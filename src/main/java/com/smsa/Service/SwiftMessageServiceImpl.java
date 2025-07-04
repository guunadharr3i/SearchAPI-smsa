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
import java.lang.reflect.InvocationTargetException;
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

    private List<Predicate> buildDynamicPredicates(SwiftMessageHeaderFilterPojo filter, CriteriaBuilder cb, Root<SwiftMessageHeader> root) {
        List<Predicate> predicates = new ArrayList<>();

        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(SwiftMessageHeaderFilterPojo.class).getPropertyDescriptors()) {
                String fieldName = pd.getName();
                Object value;

                if (!"class".equals(fieldName)) {
                    value = pd.getReadMethod().invoke(filter);
                    if (value != null) {
                        Predicate predicate = buildPredicateForField(fieldName, value, cb, root);
                        if (predicate != null) {
                            predicates.add(predicate);
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
        if (fieldName.endsWith("From") && value instanceof Comparable) {
            return cb.greaterThanOrEqualTo(root.get(fieldName.replace("From", "")), (Comparable) value);
        }

        if (fieldName.endsWith("To") && value instanceof Comparable) {
            return cb.lessThanOrEqualTo(root.get(fieldName.replace("To", "")), (Comparable) value);
        }

        if (value instanceof List) {
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

        if (list.get(0) instanceof String) {
            List<Predicate> likePredicates = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    likePredicates.add(cb.like(cb.lower(root.get(fieldName)), "%" + escapeLike(item.toString().toLowerCase()) + "%"));
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
        pojo.setRawTxt(entity.getRawMessageData());

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

}
