package com.smsa.repository;

//package com.swift.parser.repository;
//
//
//import com.swift.parser.dto.SwiftMessageDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class SwiftGetRepository {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public List<SwiftMessageDTO> searchSwiftMessages(Map<String, String> filters, int page, int size) {
//        StringBuilder sql = new StringBuilder("SELECT * FROM SWIFT_MESSAGES WHERE 1=1");
//
//        List<Object> params = new ArrayList<>();
//
//        if (filters.containsKey("mur")) {
//            sql.append(" AND MUR = ?");
//            params.add(filters.get("mur"));
//        }
//
//        if (filters.containsKey("senderBic")) {
//            sql.append(" AND SENDER_BIC = ?");
//            params.add(filters.get("senderBic"));
//        }
//
//        if (filters.containsKey("messageType")) {
//            sql.append(" AND MESSAGE_TYPE = ?");
//            params.add(filters.get("messageType"));
//        }
//
//        sql.append(" ORDER BY CREATED_AT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
//        params.add(page * size);
//        params.add(size);
//
//        return jdbcTemplate.query(sql.toString(), params.toArray(), (rs, rowNum) -> {
//            SwiftMessageDTO dto = new SwiftMessageDTO();
////            dto.setMessageId(rs.getLong("MESSAGE_ID"));
////            dto.setMur(rs.getString("MUR"));
////            dto.setSenderBic(rs.getString("SENDER_BIC"));
////            dto.setReceiverBic(rs.getString("RECEIVER_BIC"));
////            dto.setMessageType(rs.getString("MESSAGE_TYPE"));
////            dto.setCreatedAt(rs.getTimestamp("CREATED_AT"));
//            return dto;
//        });
//    }
//}