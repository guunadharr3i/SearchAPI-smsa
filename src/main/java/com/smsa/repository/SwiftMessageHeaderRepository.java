package com.smsa.repository;

import com.smsa.entity.SwiftMessageHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;

@Repository
public interface SwiftMessageHeaderRepository extends JpaRepository<SwiftMessageHeader, Long> {

    @Query("SELECT s.inpOut, COUNT(s) FROM SwiftMessageHeader s GROUP BY s.inpOut")
    List<Object[]> countBySmsaMsgIoGroup();

    List<SwiftMessageHeader> findTop5ByOrderByDateDesc();

    @Query(value
            = "SELECT JSON_VALUE(SMSA_SENDER_OBJ, '$.Country') AS senderCountry, "
            + "JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country') AS receiverCountry, "
            + "COUNT(*) AS transactionCount "
            + "FROM SMSA_PRT_MESSAGE_HDR "
            + "WHERE JSON_VALUE(SMSA_SENDER_OBJ, '$.Country') IS NOT NULL "
            + "AND JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country') IS NOT NULL "
            + "GROUP BY JSON_VALUE(SMSA_SENDER_OBJ, '$.Country'), "
            + "JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country')",
            nativeQuery = true)
    List<Object[]> getSenderReceiverCountryCountsRaw();

    @Query(value = "SELECT s.senderBic, COUNT(s) AS total_count "
            + "FROM SwiftMessageHeader s "
            + "WHERE s.senderBic LIKE 'ICIC%' "
            + "GROUP BY s.senderBic "
            + "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopSenderBicsStartingWithICIC(Pageable pageable);

    @Query(value = "SELECT s.receiverBic, COUNT(s) AS total_count "
            + "FROM SwiftMessageHeader s "
            + "WHERE s.receiverBic LIKE 'ICIC%' "
            + "GROUP BY s.receiverBic "
            + "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopReciverBicsStartingWithICIC(Pageable pageable);

    @Query("SELECT DISTINCT s.msgType FROM SwiftMessageHeader s ORDER BY s.msgType asc")
    List<String> findDistinctSmsaMsgTypesOrdered();

    @Query("SELECT DISTINCT s.messageId from SwiftMessageHeader s  where s.transactionRef=:transactionRef order by s.messageId")
    List<Long> findDistinctSmsaMessageIdOrdered(String transactionRef);

}
