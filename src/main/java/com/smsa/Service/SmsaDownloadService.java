/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;

import com.smsa.DTO.SmsaDownloadResponsePojo;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.entity.SwiftMessageHeader;
import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author abcom
 */
@Service
public class SmsaDownloadService {

    private static final Logger logger = LogManager.getLogger(SmsaDownloadService.class);

    @Autowired
    SwiftMessageServiceImpl mainService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final int BATCH_SIZE = 50000; // safer than 1L, can tune later

    public List<SmsaDownloadResponsePojo> filterDownloadData(SwiftMessageHeaderFilterPojo filter) {
        List<SmsaDownloadResponsePojo> resultList = new ArrayList<>();
        try {
            logger.info("➡️ Starting filterDownloadData with filter: {}", filter);

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SmsaDownloadResponsePojo> query = cb.createQuery(SmsaDownloadResponsePojo.class);
            Root<SwiftMessageHeader> root = query.from(SwiftMessageHeader.class);
            List<Predicate> predicates = mainService.buildDynamicPredicates(filter, cb, root);

            query.select(cb.construct(
                    SmsaDownloadResponsePojo.class,
                    root.get("messageId"),
                    root.get("inpOut"),
                    root.get("senderBic"),
                    root.get("receiverBic"),
                    root.get("msgType"),
                    root.get("transactionRef"),
                    root.get("transactionRelatedRefNo"),
                    root.get("fileDate"),
                    root.get("fileTime"),
                    root.get("currency"),
                    cb.function("TO_CHAR", String.class, root.get("transactionAmount")),
                    root.get("miorRef"),
                    root.get("fileType")
            )).distinct(true);

            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
                logger.info("✅ Added {} predicates to query", predicates.size());
            }

            // Sorting
            List<Order> orderOfSorting = new ArrayList<>();
            if (filter.getColumnSort() == null || filter.getColumnSort().isEmpty()) {
                filter.setColumnSort(new ArrayList<>());
            }
            if (!filter.getColumnSort().contains("fileDate")) {
                filter.getColumnSort().add("fileDate");
            }

            for (String column : filter.getColumnSort()) {
                logger.info("Sorting by column: {} {}", column, filter.getSortType());
                if ("DESC".equalsIgnoreCase(filter.getSortType())) {
                    orderOfSorting.add(cb.desc(root.get(column)));
                } else {
                    orderOfSorting.add(cb.asc(root.get(column)));
                }
            }
            query.orderBy(orderOfSorting);

            // Pagination + fetch
            TypedQuery<SmsaDownloadResponsePojo> typedQuery = entityManager.createQuery(query);
            typedQuery.setHint("org.hibernate.fetchSize", 5000);
            resultList = typedQuery.getResultList();
            logger.info("✅ Retrieved {} records from SwiftMessageHeader", resultList.size());

            // Fetch message text if required
            if (filter.isWithMsgText()) {
                logger.info("➡️ Fetching message text for {} records", resultList.size());

                List<Long> messageIds = resultList.stream()
                        .map(SmsaDownloadResponsePojo::getMessageId)
                        .collect(Collectors.toList());

                Map<Long, String> msgTextMap = getMessagesByIds(messageIds);

                resultList.forEach(pojo -> {
                    String msgText = msgTextMap.getOrDefault(pojo.getMessageId(), "");
                    pojo.setmText(msgText);
                });

                logger.info("✅ Added message text for {} records", msgTextMap.size());
            }

        } catch (Exception e) {
            logger.error("❌ Exception occurred in filterDownloadData", e);
        }
        return resultList;
    }

    @Transactional
    public Map<Long, String> getMessagesByIds(List<Long> messageIds) {
        Map<Long, String> result = new HashMap<>();

        if (messageIds == null || messageIds.isEmpty()) {
            return result;
        }

        // Step 1: Truncate temp table (if you’re using a temp table approach)
        jdbcTemplate.execute("TRUNCATE TABLE temp_message_ids");

        // Step 2: Insert message IDs in batches
        for (int i = 0; i < messageIds.size(); i += BATCH_SIZE) {
            List<Long> batch = messageIds.subList(i, Math.min(i + BATCH_SIZE, messageIds.size()));
            jdbcTemplate.batchUpdate("INSERT INTO temp_message_ids (id) VALUES (?)",
                    batch,
                    BATCH_SIZE,
                    (ps, id) -> ps.setLong(1, id));
        }

        // Step 3: Query with JOINs, fetch LOBs as-is (no CAST)
        String sql = "SELECT i.SMSA_MESSAGE_ID, i.SMSA_INST_RAW, "
                + "       h.SMSA_HDR_TEXT, "
                + "       t.SMSA_MSG_RAW, "
                + "       tr.SMSA_TRL_RAW "
                + "FROM SMSA_INST_TXT i "
                + "LEFT JOIN SMSA_PRT_MESSAGE_HDR h ON i.SMSA_MESSAGE_ID = h.SMSA_MESSAGE_ID "
                + "LEFT JOIN SMSA_MSG_TXT t ON i.SMSA_MESSAGE_ID = t.SMSA_MESSAGE_ID "
                + "LEFT JOIN SMSA_TRL_TXT tr ON i.SMSA_MESSAGE_ID = tr.SMSA_MESSAGE_ID "
                + "JOIN temp_message_ids tmp ON i.SMSA_MESSAGE_ID = tmp.id";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql).getResultList();

        // Step 4: Convert CLOBs safely
        for (Object[] row : rows) {
            Long messageId = ((Number) row[0]).longValue();

            String instRaw = clobToString(row[1]);
            String hdrText = clobToString(row[2]);
            String msgRaw = clobToString(row[3]);
            String trlRaw = clobToString(row[4]);

            String finalMsg = Stream.of(instRaw, hdrText, msgRaw, trlRaw)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));

            result.put(messageId, finalMsg);
        }

        return result;
    }

    private String clobToString(Object clobObj) {
        if (clobObj == null) {
            return "";
        }
        if (clobObj instanceof String) {
            return (String) clobObj;
        }

        if (clobObj instanceof Clob) {
            try (Reader reader = ((Clob) clobObj).getCharacterStream(); BufferedReader br = new BufferedReader(reader)) {
                return br.lines().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                throw new RuntimeException("Error reading CLOB", e);
            }
        }
        return clobObj.toString();
    }
}
