/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.repository;

import com.smsa.entity.SwiftMessageHeaderIsec;
import com.smsa.entity.SwiftMessageHeaderIsec;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

@Repository
public interface SwiftMessageHeaderIsecRepository extends JpaRepository<SwiftMessageHeaderIsec, Long> {

    // 1. Count total records
    @Query("SELECT COUNT(s) FROM SwiftMessageHeaderIsec s WHERE s.geoId IN :geoIds")
    long countAllRecords(@Param("geoIds")List<String> geoIds);

    @Query("SELECT COUNT(s) FROM SwiftMessageHeaderIsec s WHERE s.inpOut = 'I' AND s.geoId IN :geoIds")
    long countInputRefs(@Param("geoIds") List<String> geoIds);

    @Query("SELECT COUNT(s) FROM SwiftMessageHeaderIsec s WHERE s.inpOut='O' AND s.geoId IN :geoIds")
    long countOutputRefs(@Param("geoIds")List<String> geoIds);

    // 4. Distinct messageType with counts
    @Query("SELECT s.msgType, COUNT(s) FROM SwiftMessageHeaderIsec s GROUP BY s.msgType")
    List<Object[]> countByMessageType(@Param("geoIds") List<String> geoIds);

    @Query("SELECT s.senderBic, COUNT(s) AS total_count "
            + "FROM SwiftMessageHeaderIsec s "
            + "WHERE s.senderBic LIKE 'ICIC%' "
            + "  AND SUBSTRING(s.senderBic, 5, 2) IN :geoIds "
            + "GROUP BY s.senderBic "
            + "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopSenderBicsStartingWithICIC(Pageable pageable, @Param("geoIds") List<String> geoIds);

    @Query("SELECT s.receiverBic, COUNT(s) AS total_count "
            + "FROM SwiftMessageHeaderIsec s "
            + "WHERE s.receiverBic LIKE 'ICIC%' "
            + "  AND SUBSTRING(s.receiverBic, 5, 2) IN :geoIds "
            + "GROUP BY s.receiverBic "
            + "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopReceiverBicsStartingWithICIC(Pageable pageable, @Param("geoIds") List<String> geoIds);

    @Query(
            value = "WITH sender_counts AS ( "
            + "    SELECT SUBSTR(smsa_sender_bic, 5, 2) AS SMSA_GEO_ID, COUNT(*) AS sent_count "
            + "    FROM ISEC_PRT_MESSAGE_HDR "
            + "    WHERE smsa_sender_bic LIKE 'ICIC%' "
            + "      AND SUBSTR(smsa_sender_bic, 5, 2) IN (:geoIds) "
            + "    GROUP BY SUBSTR(smsa_sender_bic, 5, 2) "
            + "), "
            + "receiver_counts AS ( "
            + "    SELECT SUBSTR(smsa_receiver_bic, 5, 2) AS SMSA_GEO_ID, COUNT(*) AS received_count "
            + "    FROM ISEC_PRT_MESSAGE_HDR "
            + "    WHERE smsa_receiver_bic LIKE 'ICIC%' "
            + "      AND SUBSTR(smsa_receiver_bic, 5, 2) IN (:geoIds) "
            + "    GROUP BY SUBSTR(smsa_receiver_bic, 5, 2) "
            + ") "
            + "SELECT COALESCE(sc.SMSA_GEO_ID, rc.SMSA_GEO_ID) AS SMSA_GEO_ID, "
            + "       NVL(sc.sent_count, 0) AS sent_count, "
            + "       NVL(rc.received_count, 0) AS received_count "
            + "FROM sender_counts sc "
            + "FULL OUTER JOIN receiver_counts rc ON sc.SMSA_GEO_ID = rc.SMSA_GEO_ID "
            + "ORDER BY sent_count DESC, received_count DESC",
            nativeQuery = true
    )
    List<Object[]> getCountryWiseICICCountsRaw(@Param("geoIds") List<String> geoIds);

    @Query(value = "SELECT year, count_per_year FROM ( "
            + " SELECT TO_CHAR(TO_DATE(smsa_file_date, 'DD-MON-RR'), 'YYYY') AS year, "
            + "        COUNT(*) AS count_per_year "
            + " FROM ISEC_PRT_MESSAGE_HDR "
            + " WHERE smsa_geo_id IN (:geoIds) "
            + " GROUP BY TO_CHAR(TO_DATE(smsa_file_date, 'DD-MON-RR'), 'YYYY') "
            + " ORDER BY year DESC "
            + ") WHERE ROWNUM <= 5",
            nativeQuery = true)
    List<Object[]> getYearWiseSmsaFileCount(@Param("geoIds") List<String> geoIds);

    @Query(value = "SELECT "
            + "    h.SMSA_MESSAGE_ID, "
            + "    TO_CHAR(h.smsa_file_date, 'YYYY-MM-DD') AS smsaFileDate, "
            + "    h.smsa_mt_code, "
            + "    h.smsa_msg_currency, "
            + "    TO_CHAR(h.smsa_txn_amount) AS smsaTxnAmount, "
            + "    CASE "
            + "        WHEN h.smsa_mt_code = 300 "
            + "        THEN JSON_VALUE(h.smsa_msg_full_json, '$.messageText.\"33B_obj\".Currency') "
            + "        ELSE h.smsa_msg_currency "
            + "    END AS effective_currency, "
            + "    CASE "
            + "        WHEN h.smsa_mt_code = 300 "
            + "        THEN REGEXP_REPLACE( "
            + "                 JSON_VALUE(h.smsa_msg_full_json, '$.messageText.\"33B_obj\".Amount'), "
            + "                 '[^0-9,]', '' "
            + "             ) "
            + "        ELSE TO_CHAR(h.smsa_txn_amount) "
            + "    END AS effective_amount "
            + "FROM ISEC_PRT_MESSAGE_HDR h "
            + "WHERE h.smsa_file_date BETWEEN :startDate AND :endDate "
            + "  AND h.smsa_geo_id IN (:geoIds) "
            + "ORDER BY h.smsa_file_date",
            nativeQuery = true)
    List<Object[]> fetchMessagesWithEffectiveValues(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("geoIds") List<String> geoIds
    );
    
    @Query("SELECT s.inpOut, COUNT(s) FROM SwiftMessageHeaderIsec s GROUP BY s.inpOut")
    List<Object[]> countBySmsaMsgIoGroup();

    List<SwiftMessageHeaderIsec> findTop5ByOrderByDateDesc();

    @Query(value
            = "SELECT JSON_VALUE(SMSA_SENDER_OBJ, '$.Country') AS senderCountry, "
            + "JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country') AS receiverCountry, "
            + "COUNT(*) AS transactionCount "
            + "FROM ISEC_PRT_MESSAGE_HDR "
            + "WHERE JSON_VALUE(SMSA_SENDER_OBJ, '$.Country') IS NOT NULL "
            + "AND JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country') IS NOT NULL "
            + "GROUP BY JSON_VALUE(SMSA_SENDER_OBJ, '$.Country'), "
            + "JSON_VALUE(SMSA_RECEIVER_OBJ, '$.Country')",
            nativeQuery = true)
    List<Object[]> getSenderReceiverCountryCountsRaw();

    @Query(value = "SELECT s.senderBic, COUNT(s) AS total_count "
            + "FROM SwiftMessageHeaderIsec s "
            + "WHERE s.senderBic LIKE 'ICIC%' "
            + "GROUP BY s.senderBic "
            + "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopSenderBicsStartingWithICIC(Pageable pageable);

    @Query(value = "SELECT s.receiverBic, COUNT(s) AS total_count "
            + "FROM SwiftMessageHeaderIsec s "
            + "WHERE s.receiverBic LIKE 'ICIC%' "
            + "GROUP BY s.receiverBic "
            + "ORDER BY COUNT(s) DESC")
    List<Object[]> findTopReciverBicsStartingWithICIC(Pageable pageable);


    @Query("SELECT DISTINCT s.messageId from SwiftMessageHeaderIsec s  where s.transactionRef=:transactionRef order by s.messageId")
    List<Long> findDistinctSmsaMessageIdOrdered(String transactionRef);

    @Query(value = "SELECT SMSA_MSG_TYPE "
            + "FROM ISEC_PRT_MESSAGE_HDR "
            + "WHERE SMSA_GEO_ID IN (:geoIds)",
            nativeQuery = true)
    List<String> findMsgTypesByGeoIds(@Param("geoIds") List<String> geoIds);

}
