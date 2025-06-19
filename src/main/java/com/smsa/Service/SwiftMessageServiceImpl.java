package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.entity.SwiftMessageHeader;
import com.smsa.repository.SwiftMessageHeaderRepository;
import java.lang.reflect.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;
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
     */
    @Override
//    @Cacheable(value = "swiftMessages", key = "'filter_'+#filter.transactionRef")
    public Page<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderPojo filter, Pageable pageable) {
        logger.info("Executing getFilteredMessages with filter: {}", filter);

        List<SwiftMessageHeader> resultList = new ArrayList<>();
        long totalCount = 0;

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            CriteriaQuery<SwiftMessageHeader> query = cb.createQuery(SwiftMessageHeader.class);
            Root<SwiftMessageHeader> root = query.from(SwiftMessageHeader.class);
            List<Predicate> predicates = buildDynamicPredicates(filter, cb, root);

            query.select(root).distinct(true);
            if (!predicates.isEmpty()) {
                query.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<SwiftMessageHeader> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
            resultList = typedQuery.getResultList();

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

        List<SwiftMessageHeaderPojo> pojoList = resultList.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());

        return new PageImpl<>(pojoList, pageable, totalCount);
    }

    private List<Predicate> buildDynamicPredicates(SwiftMessageHeaderPojo filter, CriteriaBuilder cb, Root<SwiftMessageHeader> root) {
        List<Predicate> predicates = new ArrayList<>();

        Field[] fields = SwiftMessageHeaderPojo.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(filter);

                if (value != null) {
                    String fieldName = field.getName();

                    // Handle "From" and "To" suffix for ranges
                    if (fieldName.endsWith("From") && value instanceof Comparable) {
                        String baseField = fieldName.replace("From", "");
                        predicates.add(cb.greaterThanOrEqualTo(root.get(baseField), (Comparable) value));
                    } else if (fieldName.endsWith("To") && value instanceof Comparable) {
                        String baseField = fieldName.replace("To", "");
                        predicates.add(cb.lessThanOrEqualTo(root.get(baseField), (Comparable) value));
                    } // Normal string LIKE search
                    else if (value instanceof String && !((String) value).trim().isEmpty()) {
                        predicates.add(cb.like(cb.lower(root.get(fieldName)), "%" + escapeLike(((String) value).toLowerCase()) + "%"));
                    } // EQUAL for non-strings
                    else if (!(value instanceof String)) {
                        predicates.add(cb.equal(root.get(fieldName), value));
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                logger.warn("Could not access field: {}", field.getName(), e);
            }
        }

        return predicates;
    }

    private SwiftMessageHeaderPojo mapToPojo(SwiftMessageHeader entity) {
        SwiftMessageHeaderPojo pojo = new SwiftMessageHeaderPojo();
        pojo.setMessageId(entity.getId());
        pojo.setFileName(entity.getFileName());
        pojo.setDate(entity.getDate());
        pojo.setTime(entity.getTime());
        pojo.setMtCode(entity.getMtCode());
        pojo.setPage(entity.getPage());
        pojo.setPriority(entity.getPriority());
        pojo.setFileType(entity.getFileType());
        pojo.setInputRefNo(entity.getInputRefNo());
        pojo.setOutputRefNo(entity.getOutputRefNo());
        pojo.setInpOut(entity.getInpOut());
        pojo.setMsgDesc(entity.getMsgDesc());
        pojo.setMsgType(entity.getMsgType());
        pojo.setSlaId(entity.getSlaId());
        pojo.setSenderBic(entity.getSenderBic());
        pojo.setSenderBicDesc(entity.getSenderBicDesc());
        pojo.setReceiverBic(entity.getReceiverBic());
        pojo.setReceiverBicDesc(entity.getReceiverBicDesc());
        pojo.setUserRef(entity.getUserRef());
        pojo.setTransactionRef(entity.getTransactionRef());
        pojo.setFileDate(entity.getFileDate());
        pojo.setMur(entity.getMur());
        pojo.setUetr(entity.getUetr());

        return pojo;
    }

    @Override
    public List<SwiftMessageHeader> getFullData() {
        return repository.findAll();
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
    public List<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderPojo filters) {
         List<SwiftMessageHeader> resultList = new ArrayList<>();
        long totalCount = 0;

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
            resultList = typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("Exception occurred while filtering Swift messages: {}", e.getMessage(), e);
        }

        List<SwiftMessageHeaderPojo> pojoList = resultList.stream()
                .map(this::mapToPojo)
                .collect(Collectors.toList());

        return pojoList;
    }
}
