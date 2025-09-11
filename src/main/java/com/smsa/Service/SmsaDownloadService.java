/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;

import com.smsa.DTO.SmsaDownloadResponsePojo;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.entity.SwiftMessageHeader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    public List<SmsaDownloadResponsePojo> filterDownloadData(SwiftMessageHeaderFilterPojo filter) {
        List<SmsaDownloadResponsePojo> resultList = new ArrayList<>();
        try {
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
            }

            // ✅ Sorting
            List<Order> orderOfSorting = new ArrayList<>();
            if (filter.getColumnSort() == null || filter.getColumnSort().isEmpty()) {
                filter.setColumnSort(new ArrayList<>());
            }
            if (!filter.getColumnSort().contains("fileDate")) {
                filter.getColumnSort().add("fileDate");
            }

            for (String column : filter.getColumnSort()) {
                logger.info("Sorting by column: {}", column);
                if ("DESC".equalsIgnoreCase(filter.getSortType())) {
                    orderOfSorting.add(cb.desc(root.get(column)));
                } else {
                    orderOfSorting.add(cb.asc(root.get(column)));
                }
            }
            query.orderBy(orderOfSorting);

            // ✅ Pagination
            TypedQuery<SmsaDownloadResponsePojo> typedQuery = entityManager.createQuery(query);
            typedQuery.setHint("org.hibernate.fetchSize", 5000);
            resultList = typedQuery.getResultList();
            logger.info("Is mesageText enabled: " + filter.isWithMsgText());
            if (filter.isWithMsgText()) {
                List<Long> messageIds = resultList.stream()
                        .map(SmsaDownloadResponsePojo::getMessageId)
                        .collect(Collectors.toList());
                Map<Long, String> msgTextMap = getMessagesByIds(messageIds);
                resultList.forEach(pojo -> {
                    String msgText = msgTextMap.get(pojo.getMessageId()) == null ? "" : msgTextMap.get(pojo.getMessageId());
                    pojo.setmText(msgText);
                });
                logger.info("result list size: " + resultList.size());
            }

        } catch (Exception e) {
            logger.error("Exception occured while fetching data pls check logs: " + e);
            logger.info("Exception: " + e);
        }
        return resultList;
    }

    public Map<Long, String> getMessagesByIds(List<Long> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return Collections.emptyMap();
        }

        // 1️⃣ Truncate temp table
        jdbcTemplate.update("TRUNCATE TABLE temp_message_ids");

        // 2️⃣ Batch insert IDs
        int batchSize = 50000;
        for (int i = 0; i < messageIds.size(); i += batchSize) {
            List<Long> batch = messageIds.subList(i, Math.min(i + batchSize, messageIds.size()));
            jdbcTemplate.batchUpdate("INSERT INTO temp_message_ids (message_id) VALUES (?)",
                    batch,
                    batch.size(),
                    (ps, id) -> ps.setLong(1, id));
        }

        // 3️⃣ Fetch messages using EntityManager
        String sql = "SELECT i.SMSA_MESSAGE_ID, i.SMSA_INST_RAW, "
                + "h.SMSA_HDR_TEXT, t.SMSA_MSG_RAW, tr.SMSA_TRL_RAW "
                + "FROM SMSA_INST_TXT i "
                + "LEFT JOIN SMSA_PRT_MESSAGE_HDR h ON i.SMSA_MESSAGE_ID = h.SMSA_MESSAGE_ID "
                + "LEFT JOIN SMSA_MSG_TXT t ON i.SMSA_MESSAGE_ID = t.SMSA_MESSAGE_ID "
                + "LEFT JOIN SMSA_MSG_TRL tr ON i.SMSA_MESSAGE_ID = tr.SMSA_MESSAGE_ID "
                + "JOIN temp_message_ids tmp ON i.SMSA_MESSAGE_ID = tmp.message_id";

        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();

        // 4️⃣ Process results
        Map<Long, String> msgText = results.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),
                        row -> {
                            String instRaw = clobToString(row[1]);
                            String hdrText = clobToString(row[2]);
                            String msgRaw = clobToString(row[3]);
                            String trlRaw = clobToString(row[4]);

                            return (instRaw == null ? "" : instRaw) + "\n"
                            + (hdrText == null ? "" : hdrText) + "\n"
                            + (msgRaw == null ? "" : msgRaw) + "\n"
                            + (trlRaw == null ? "" : trlRaw);
                        },
                        (v1, v2) -> v1
                ));

        return msgText;
    }

    private String clobToString(Object clobObj)  {
        if (clobObj == null) {
            return "";
        }
        if (clobObj instanceof String) {
            return (String) clobObj;
        }

        if (clobObj instanceof Clob) {
            Clob clob = (Clob) clobObj;
        StringBuilder sb = new StringBuilder();
        try (Reader reader = clob.getCharacterStream()) {
                if (reader == null) {
                    return "";
                }
                char[] buffer = new char[8192]; // read in 8KB chunks
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, charsRead);
            }
            } catch (SQLException | IOException e) {
                throw new RuntimeException("Failed to convert CLOB to String", e);
        }
        return sb.toString();
    }

        return clobObj.toString();
    }

}
