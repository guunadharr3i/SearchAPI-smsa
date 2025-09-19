package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.entity.SwiftMessageHeader;
import com.smsa.repository.SwiftMessageHeaderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
public class SwiftMessageServiceImpl implements SwiftMessageService {
    
    private static final Logger logger = LogManager.getLogger(SwiftMessageServiceImpl.class);
    
    @Autowired
    private SwiftMessageHeaderRepository repository;
    
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch filtered messages and cache results in Redis using
     * filter.toString() as key
     *
     * @param filter
     * @param pageable
     * @return
     */
    @Override
//    @Cacheable(value = "swiftMessages", key = "'filter_'+#filter.transactionRef")
    public Page<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderFilterPojo filter, Pageable pageable) {
        logger.info("Executing getFilteredMessages with filter: {}", filter);
        
        List<SwiftMessageHeaderPojo> resultList = new ArrayList<>();
        long totalCount = 0;
        
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // ✅ FIRST QUERY: Get header data without CLOB
            CriteriaQuery<SwiftMessageHeaderPojo> query = cb.createQuery(SwiftMessageHeaderPojo.class);
            Root<SwiftMessageHeader> root = query.from(SwiftMessageHeader.class);
            if (filter.getReportType().equals("Fircosoft Report")) {
                if (filter.getFircoSoftReportStatus() == null || filter.getFircoSoftReportStatus().isEmpty() || filter.getFircoSoftReportStatus().equalsIgnoreCase("ALL")) {
                    List<String> status = Arrays.asList("Pending", "Approved", "Rejected");
                    filter.setFircoSoftStatus(status);
                } else {
                    filter.setFircoSoftStatus(Arrays.asList(filter.getFircoSoftReportStatus()));
                }
            } else {
                filter.setFircoSoftStatus(null);
            }
            
            List<Predicate> predicates = buildDynamicPredicates(filter, cb, root);

            // ✅ Select only non-CLOB fields from header table
            query.select(cb.construct(
                    SwiftMessageHeaderPojo.class,
                    root.get("messageId"),
                    root.get("senderBic"),
                    root.get("receiverBic"),
                    root.get("currency"),
                    cb.function("TO_CHAR", String.class, root.get("transactionAmount")),
                    root.get("inpOut"),
                    root.get("uetr"),
                    root.get("fileDate"),
                    root.get("fileType"),
                    root.get("msgType"),
                    root.get("transactionRef"),
                    root.get("fileName"),
                    root.get("transactionRelatedRefNo"),
                    root.get("fircoSoftStatus"),
                    root.get("message2ndCopyDate"),
                    cb.nullLiteral(String.class) // Placeholder for messageText
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
            TypedQuery<SwiftMessageHeaderPojo> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
            
            resultList = typedQuery.getResultList();

            // ✅ SECOND QUERY: Fetch only messageText for the retrieved messageIds
            if (!resultList.isEmpty() && filter.getMessageId() != null && !filter.getMessageId().isEmpty()) {
                List<Long> messageIds = resultList.stream()
                        .map(SwiftMessageHeaderPojo::getMessageId)
                        .collect(Collectors.toList());
                String jpq = "SELECT t.messageId,t.rawInstance from SwiftMessageInstance t where t.messageId IN :messageIds";
                List<Object[]> instanceTexts = entityManager.createQuery(jpq, Object[].class)
                        .setParameter("messageIds", messageIds)
                        .getResultList();
                Map<Long, String> instanceTextMap = instanceTexts.stream()
                        .filter(row -> row[1] != null)
                        .collect(Collectors.toMap(
                                row -> (Long) row[0],
                                row -> row[1].toString(),
                                (v1, v2) -> v1
                        ));
                String jpq2 = "SELECT t.messageId,t.smsaHeaderText from SwiftMessageHeader t where t.messageId IN :messageIds";
                List<Object[]> hdrTexts = entityManager.createQuery(jpq2, Object[].class)
                        .setParameter("messageIds", messageIds)
                        .getResultList();
                Map<Long, String> hdrTextMap = hdrTexts.stream()
                        .filter(row -> row[1] != null)
                        .collect(Collectors.toMap(
                                row -> (Long) row[0],
                                row -> row[1].toString(),
                                (v1, v2) -> v1
                        ));
                // Create a map of messageId -> messageText
                String jpql = "SELECT t.messageId, t.raw_messageText FROM SwiftMessageText t WHERE t.messageId IN :messageIds";
                List<Object[]> messageTexts = entityManager.createQuery(jpql, Object[].class)
                        .setParameter("messageIds", messageIds)
                        .getResultList();
                
                Map<Long, String> messageTextMap = messageTexts.stream()
                        .filter(row -> row[1] != null)
                        .collect(Collectors.toMap(
                                row -> (Long) row[0],
                                row -> row[1].toString(),
                                (v1, v2) -> v1
                        ));
                String jpq3 = "SELECT t.messageId,t.trailerRaw from SwiftMessageTrailer t where t.messageId IN :messageIds";
                List<Object[]> trailerTexts = entityManager.createQuery(jpq3, Object[].class)
                        .setParameter("messageIds", messageIds)
                        .getResultList();
                Map<Long, String> trailerTextMap = trailerTexts.stream()
                        .filter(row -> row[1] != null) // ✅ skip null values
                        .collect(Collectors.toMap(
                                row -> (Long) row[0],
                                row -> row[1].toString(),
                                (v1, v2) -> v1 // handle duplicates (keep first)
                        ));

                // ✅ Set messageText in the result objects
                resultList.forEach(pojo -> {
                    String messageText = instanceTextMap.get(pojo.getMessageId()) + "\n" + hdrTextMap.get(pojo.getMessageId()) + "\n"
                            + trailerTextMap.get(pojo.getMessageId()) + "\n" + messageTextMap.get(pojo.getMessageId());
                    pojo.setRawTxt(messageText); // Assuming you have a setter
                });
            }

            // ✅ Count query (without CLOB join)
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<SwiftMessageHeader> countRoot = countQuery.from(SwiftMessageHeader.class);
            List<Predicate> countPredicates = buildDynamicPredicates(filter, cb, countRoot);
            
            countQuery.select(cb.countDistinct(countRoot));
            if (!countPredicates.isEmpty()) {
                countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
            }
            
            totalCount = entityManager.createQuery(countQuery).getSingleResult();
            
        } catch (Exception e) {
            logger.error("Exception occurred while filtering Swift messages: {}", e.getMessage(), e);
        }
        
        return new PageImpl<>(resultList, pageable, totalCount);
    }
    
    public List<Predicate> buildDynamicPredicates(SwiftMessageHeaderFilterPojo filter, CriteriaBuilder cb, Root<SwiftMessageHeader> root) {
        List<Predicate> predicates = new ArrayList<>();
        
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(SwiftMessageHeaderFilterPojo.class).getPropertyDescriptors()) {
                String fieldName = pd.getName();
                Object value;
                
                if (!"reportType".equals(fieldName)&&!"fircoSoftReportStatus".equals(fieldName) && !"class".equals(fieldName) && !"sortType".equals(fieldName) && !"columnSort".equals(fieldName) && !"withMsgText".equals(fieldName)) {
                    value = pd.getReadMethod().invoke(filter);
                    if (value != null) {
                        if (value instanceof List) {
                            List<?> rawList = (List<?>) value;

                            // Remove nulls and empty strings with only spaces
                            List<?> filteredList = rawList.stream()
                                    .filter(Objects::nonNull)
                                    .filter(item -> !(item instanceof String) || !((String) item).trim().isEmpty())
                                    .collect(Collectors.toList());
                            
                            if (!filteredList.isEmpty()) {
                                Predicate predicate = buildPredicateForField(fieldName, filteredList, cb, root);
                                if (predicate != null) {
                                    predicates.add(predicate);
                                }
                            }
                        } else {
                            if (value instanceof String) {
                                String val = (String) value;
                                if (!val.trim().isEmpty()) {
                                    Predicate predicate = buildPredicateForField(fieldName, value, cb, root);
                                    if (predicate != null) {
                                        predicates.add(predicate);
                                    }
                                }
                            } else {
                                Predicate predicate = buildPredicateForField(fieldName, value, cb, root);
                                if (predicate != null) {
                                    predicates.add(predicate);
                                }
                            }
                        }
                        
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Error building dynamic predicates", e);
        }
        
        return predicates;
    }
    
    private Predicate buildPredicateForField(String fieldName, Object value, CriteriaBuilder cb, Root<SwiftMessageHeader> root) {
        if (fieldName.equals("fromTime") && value instanceof String) {
            return cb.greaterThanOrEqualTo(root.get("fileTime"), (String) value);
        }
        if (fieldName.equals("toTime") && value instanceof String) {
            return cb.lessThanOrEqualTo(root.get("fileTime"), (String) value);
        }
        if (fieldName.equals("fromAmount") && value instanceof String) {
            return cb.greaterThanOrEqualTo(root.get("transactionAmount"), new BigDecimal((String) value));
        }
        if (fieldName.equals("toAmount") && value instanceof String) {
            return cb.lessThanOrEqualTo(root.get("transactionAmount"), new BigDecimal((String) value));
        }
        
        if (fieldName.endsWith("From") && value instanceof Comparable) {
            return cb.greaterThanOrEqualTo(root.get("fileDate"), (Comparable) value);
        }
        
        if (fieldName.endsWith("To") && value instanceof Comparable) {
            return cb.lessThanOrEqualTo(root.get("fileDate"), (Comparable) value);
        }
        
        if (value instanceof List && value != null && !((List<?>) value).isEmpty()) {
            return handleListPredicate(fieldName, (List<?>) value, cb, root);
        }
        
        if (value instanceof String) {
            String str = ((String) value).trim();
            if (!str.isEmpty()) {
                return cb.like(cb.lower(root.get(fieldName)), "%" + escapeLike(str.toLowerCase()) + "%");
            }
            return null;
        }
        
        return cb.equal(root.get(fieldName), value);
    }
    
    private Predicate handleListPredicate(String fieldName, List<?> list, CriteriaBuilder cb, Root<SwiftMessageHeader> root) {
        if (list.isEmpty()) {
            return null;
        }
        
        List<Predicate> likePredicates = new ArrayList<>();
        
        if (list.get(0) instanceof String) {
            for (Object item : list) {
                if (item != null) {
                    // Convert column to string using TO_CHAR
                    Expression<String> fieldAsString = cb.function("TO_CHAR", String.class, root.get(fieldName));
                    likePredicates.add(
                            cb.like(cb.lower(fieldAsString), "%" + escapeLike(item.toString().toLowerCase()) + "%")
                    );
                }
            }
            return cb.or(likePredicates.toArray(new Predicate[0]));
        }
        
        return root.get(fieldName).in(list);
    }
    
    private SwiftMessageHeaderPojo mapToPojo(SwiftMessageHeader entity) {
        SwiftMessageHeaderPojo pojo = new SwiftMessageHeaderPojo();
        pojo.setMessageId(entity.getId());
        pojo.setFileName(entity.getFileName());
        pojo.setFileType(entity.getFileType());
        pojo.setInpOut(entity.getInpOut());
        pojo.setMsgType(entity.getMsgType());
        pojo.setSenderBic(entity.getSenderBic());
        pojo.setReceiverBic(entity.getReceiverBic());
        pojo.setTransactionRef(entity.getTransactionRef());
        pojo.setFileDate(entity.getFileDate());
        pojo.setUetr(entity.getUetr());
        pojo.setCurrency(entity.getCurrency());
        pojo.setTransactionAmount(entity.getTransactionAmount() == null ? null : entity.getTransactionAmount().toString());
        return pojo;
    }
    
    @Override
    public List<SwiftMessageHeaderPojo> getFullData() {
        logger.info("Fetching top 5 SwiftMessageHeader records by date");
        
        try {
            List<SwiftMessageHeader> entities = repository.findTop5ByOrderByDateDesc();
            
            if (entities == null || entities.isEmpty()) {
                logger.warn("No SwiftMessageHeader records found");
                return Collections.emptyList();
            }
            
            List<SwiftMessageHeaderPojo> pojos = entities.stream()
                    .map(this::mapToPojo)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            logger.info("Successfully mapped {} entities to POJOs", pojos.size());
            return pojos;
            
        } catch (Exception e) {
            logger.error("Error while fetching or processing SwiftMessageHeader data", e);
            return Collections.emptyList();
        }
    }
    
    public long totalRecords() {
        long count = repository.count();
        logger.info("Total records in SwiftMessageHeader: {}", count);
        return count;
    }
    
    public Map<String, Long> getCountByMsgIo() {
        logger.info("Executing getCountByMsgIo");
        Map<String, Long> countMap = new HashMap<>();
        
        try {
            List<Object[]> result = repository.countBySmsaMsgIoGroup();
            
            for (Object[] row : result) {
                String type = (String) row[0];   // 'I' or 'O'
                Long count = (Long) row[1];      // count
                countMap.put(type, count);
                logger.debug("Message type '{}' has count: {}", type, count);
            }
            
        } catch (Exception e) {
            logger.error("Exception in getCountByMsgIo: {}", e.getMessage(), e);
        }
        
        return countMap;
    }
    
    private String escapeLike(String param) {
        return param.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%");
    }
    
    @Override
    public List<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderFilterPojo filters) {
        List<SwiftMessageHeaderPojo> pojoList = new ArrayList<>();
        
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            
            CriteriaQuery<SwiftMessageHeader> query = cb.createQuery(SwiftMessageHeader.class);
            Root<SwiftMessageHeader> root = query.from(SwiftMessageHeader.class);
            List<Predicate> predicates = buildDynamicPredicates(filters, cb, root);
            
            query.select(root).distinct(true);
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }
            
            TypedQuery<SwiftMessageHeader> typedQuery = entityManager.createQuery(query);

            // If using a relational DB, this helps JDBC layer.
            typedQuery.setHint("org.hibernate.fetchSize", 1000);

            // Stream processing — better memory usage
            try (Stream<SwiftMessageHeader> stream = typedQuery.getResultList().stream()) {
                pojoList = stream
                        .map(this::mapToPojo)
                        .collect(Collectors.toList());
            }
            
        } catch (Exception e) {
            logger.error("Exception occurred while filtering Swift messages: {}", e.getMessage(), e);
        }
        
        return pojoList;
    }
    
    @Override
    public List<SwiftMessageHeaderPojo> getTotalData() {
        List<SwiftMessageHeader> resultList = repository.findAll();
        List<SwiftMessageHeaderPojo> pojoList = resultList.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());
        return pojoList;
    }
    
    public List<String> getMessageTypes() {
        return repository.findDistinctSmsaMsgTypesOrdered();
    }
    
    @Override
    public List<String> getRawData(String transactionRef) {
        List<Long> msgIds = repository.findDistinctSmsaMessageIdOrdered(transactionRef);
        List<String> rawData = getMessagesByIds(msgIds);
        return rawData;
    }
    
    public List<String> getMessagesByIds(List<Long> messageIds) {
        String sql = "SELECT i.SMSA_MESSAGE_ID, i.SMSA_INST_RAW, "
                + "       h.SMSA_HDR_TEXT, "
                + "       t.SMSA_MSG_RAW, "
                + "       tr.SMSA_TRL_RAW "
                + "FROM SMSA_INST_TXT i "
                + "LEFT JOIN SMSA_PRT_MESSAGE_HDR h ON i.SMSA_MESSAGE_ID = h.SMSA_MESSAGE_ID "
                + "LEFT JOIN SMSA_MSG_TXT t ON i.SMSA_MESSAGE_ID = t.SMSA_MESSAGE_ID "
                + "LEFT JOIN SMSA_MSG_TRL tr ON i.SMSA_MESSAGE_ID = tr.SMSA_MESSAGE_ID "
                + "WHERE i.SMSA_MESSAGE_ID IN (:messageIds)";
        
        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("messageIds", messageIds)
                .getResultList();
        return results.stream()
                .map(row -> String.join("\n",
                clobToString(row[1]),
                clobToString(row[2]),
                clobToString(row[3]),
                clobToString(row[4])
        ))
                .collect(Collectors.toList());
        
    }
    
    private String clobToString(Object clobObj) {
        if (clobObj == null) {
            return "";
        }
        if (clobObj instanceof String) {
            return (String) clobObj; // already string
        }
        if (clobObj instanceof Clob) {
            try {
                Clob clob = (Clob) clobObj;
                StringBuilder sb = new StringBuilder();
                try (Reader reader = clob.getCharacterStream(); BufferedReader br = new BufferedReader(reader)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                return sb.toString().trim();
            } catch (SQLException | java.io.IOException e) {
                throw new RuntimeException("Failed to convert CLOB to String", e);
            }
        }
        return clobObj.toString();
    }
}
