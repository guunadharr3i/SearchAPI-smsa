package com.smsa.repository;

import com.smsa.entity.SwiftMessageHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftMessageHeaderRepository extends JpaRepository<SwiftMessageHeader, Long> {

    @Query("SELECT s.inpOut, COUNT(s) FROM SwiftMessageHeader s GROUP BY s.inpOut")
    List<Object[]> countBySmsaMsgIoGroup();

    @Query("SELECT s FROM SwiftMessageHeader s WHERE s.transactionRef IN :txnRefs")
    List<SwiftMessageHeader> findByTransactionRefIn(List<String> txnRefs);

    List<SwiftMessageHeader> findTop5ByOrderByDateDesc();

    // 🔽 New native query for sender-receiver country grouping
    @Query(value = "SELECT "
            + "JSON_VALUE(senderObj, '$.Country') AS senderCountry, "
            + "JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country') AS receiverCountry, "
            + "COUNT(*) AS transactionCount "
            + "FROM SMSA_PRT_MESSAGE_HDR "
            + "WHERE JSON_VALUE(SMSA_SENDER_OBJ, '$.Country') IS NOT NULL "
            + "AND JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country') IS NOT NULL "
            + "GROUP BY JSON_VALUE(SMSA_SENDER_OBJ, '$.Country'), "
            + "JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country')",
            nativeQuery = true)
    List<Object[]> getSenderReceiverCountryCountsRaw();
}
