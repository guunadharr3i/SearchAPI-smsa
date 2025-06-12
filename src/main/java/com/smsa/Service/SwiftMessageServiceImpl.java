package com.smsa.Service;

import com.smsa.DTO.SwiftRequestPojo;
import com.smsa.entity.SwiftMessageHeader;
import com.smsa.repository.SwiftMessageHeaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Service
public class SwiftMessageServiceImpl implements SwiftMessageService {

    private static final Logger logger = LoggerFactory.getLogger(SwiftMessageServiceImpl.class);

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
    public List<SwiftMessageHeader> getFilteredMessages(SwiftRequestPojo filter) {
        System.out.println(" Executing DB query for filter: {}" + filter);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SwiftMessageHeader> query = cb.createQuery(SwiftMessageHeader.class);
            Root<SwiftMessageHeader> root = query.from(SwiftMessageHeader.class);

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTransactionRef() != null && !filter.getTransactionRef().isEmpty()) {
                predicates.add(cb.like(root.get("transactionRef"), "%" + escapeLike(filter.getTransactionRef()) + "%"));
            }

            if (filter.getSenderBic() != null && !filter.getSenderBic().isEmpty()) {
                predicates.add(cb.like(root.get("senderBic"), "%" + escapeLike(filter.getSenderBic()) + "%"));
            }

            if (filter.getFileDate() != null) {
                predicates.add(cb.equal(root.get("fileDate"), filter.getFileDate()));
            }

            if (filter.getMtCode() != null) {
                predicates.add(cb.equal(root.get("mtCode"),filter.getMtCode()));
            }

            if (filter.getMsgType() != null && !filter.getMsgType().isEmpty()) {
                predicates.add(cb.like(root.get("msgType"), "%" + escapeLike(filter.getMsgType()) + "%"));
            }

            query.where(cb.and(predicates.toArray(new Predicate[0])));
            query.select(root).distinct(true);

            TypedQuery<SwiftMessageHeader> typedQuery = entityManager.createQuery(query);

            return typedQuery.getResultList();
        } catch (Exception e) {
            logger.error("Exception occurred while filtering Swift messages", e);
        }
        return new ArrayList<>();
    }
    
    public long totalRecords(){
        return repository.count();
    }
    public Map<String, Long> getCountByMsgIo() {
        List<Object[]> result = repository.countBySmsaMsgIoGroup();

        Map<String, Long> countMap = new HashMap<>();
        for (Object[] row : result) {
            String type = (String) row[0];   // 'I' or 'O'
            Long count = (Long) row[1];      // count
            countMap.put(type, count);
        }

        return countMap;
    }


    private String escapeLike(String param) {
        return param.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("%", "\\%");
    }
}
