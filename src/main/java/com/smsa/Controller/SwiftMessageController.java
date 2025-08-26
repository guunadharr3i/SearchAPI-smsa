package com.smsa.Controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.smsa.DTO.AuthRequest;
import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.Enums.ErrorCode;
import com.smsa.ResponseWrappers.ApiResponse;
import com.smsa.Service.SwiftMessageService;
import com.smsa.Utils.EncryptedResponseData;
import com.smsa.Utils.EncryptedtPayloadRequest;
import com.smsa.encryption.AESUtil;
import com.smsa.tokenValidation.AuthenticateAPi;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class SwiftMessageController {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SwiftMessageController.class);

    @Autowired
    private SwiftMessageService service;

    @Autowired
    private AuthenticateAPi authenticateApi;

    @Value("${aes.auth.key}")
    private String secretKey;

    @Value("${aes.auth.vi.key}")
    private String viKey;

    // Reusable ObjectMapper without JavaTimeModule
    private ObjectMapper getCustomMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule customDatesModule = new SimpleModule();

        // Serializer for LocalDate (same as before)
        customDatesModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString()); // yyyy-MM-dd
            }
        });

        // Updated deserializer for LocalDate to handle blank strings gracefully
        customDatesModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String dateStr = p.getValueAsString();
                if (dateStr == null || dateStr.trim().isEmpty()) {
                    return null; // treat blank or empty string as null
                }
                return LocalDate.parse(dateStr.trim());
            }
        });

        // Serializer for LocalDateTime (same as before)
        customDatesModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString()); // yyyy-MM-ddTHH:mm:ss
            }
        });

        // Updated deserializer for LocalDateTime to handle blank strings gracefully
        customDatesModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String dateTimeStr = p.getValueAsString();
                if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
                    return null; // treat blank or empty string as null
                }
                return LocalDateTime.parse(dateTimeStr.trim());
            }
        });

        mapper.registerModule(customDatesModule);
        return mapper;
    }

    @PostMapping("/searchApi")
    public ResponseEntity<ApiResponse<String>> getFilteredMessages(
            @RequestBody EncryptedtPayloadRequest encryptedRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Received encrypted request to /searchApi");

        try {
            String decryptedJson = AESUtil.decrypt(encryptedRequest.getEncryptedPayload(), secretKey, viKey);
            ObjectMapper mapper = getCustomMapper();

            FilterRequest filter = mapper.readValue(decryptedJson, FilterRequest.class);
            logger.info("before authentication call time: " + new Date());
            String decryptedToken=AESUtil.decrypt(filter.getTokenRequest().get("token"), secretKey, viKey);
            String result = authenticateApi.validateAndRefreshToken(filter.getTokenRequest());
            String[] parts = result.split(":", 2);
            int statusCode = Integer.parseInt(parts[0]);
            String accessToken = parts.length > 1 ? parts[1] : "";
            logger.info("after call token out time: " + new Date());
            logger.info("after authentication call " + accessToken);
            if (accessToken == null) {
                ApiResponse<String> response = new ApiResponse<>(ErrorCode.TOKEN_INVALID);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if (statusCode != 200) {
                // Success → return token in body
                ApiResponse<String> response = new ApiResponse<>(statusCode, accessToken, null);

                return ResponseEntity.status(statusCode).body(response);
            }
            logger.info("Decrypted FilterRequest: {}, page: {}, size: {}", filter, page, size);

            Pageable pageable = PageRequest.of(page, size);
            Page<SwiftMessageHeaderPojo> pagedResult = service.getFilteredMessages(filter.getFilter(), pageable);

            EncryptedResponseData responseData = new EncryptedResponseData();
            responseData.setAccessToken(accessToken);
            responseData.setMessages(pagedResult.getContent());
            responseData.setTotalElements(pagedResult.getTotalElements());
            responseData.setTotalPages(pagedResult.getTotalPages());
            responseData.setCurrentPage(pagedResult.getNumber());

            String jsonResponse = mapper.writeValueAsString(responseData);
            String encryptedResponse = AESUtil.encrypt(jsonResponse, secretKey, viKey);

            return ResponseEntity.ok(new ApiResponse<>(ErrorCode.SUCCESS, encryptedResponse));

        } catch (JsonProcessingException ex) {
            logger.error("JsonProcessingException: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        } catch (Exception ex) {
            logger.error("Unexpected error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        }
    }

    @PostMapping("/getMessageTypes")
    public ResponseEntity<?> getMessageTypes(@RequestBody AuthRequest request) {
        logger.info("Request received to get messageTypes.");
        try {
            String token = request.getToken();
            String deviceHash = request.getDeviceHash();

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            tokenMap.put("DeviceHash", deviceHash);

            String result = authenticateApi.validateAndRefreshToken(tokenMap);
            String[] parts = result.split(":", 2);
            int statusCode = Integer.parseInt(parts[0]);
            String accessToken = parts.length > 1 ? parts[1] : "";
            logger.info("after call token out time: " + new Date());
            logger.info("after authentication call " + accessToken);
            if (statusCode != 200) {
                // Success → return token in body
                ApiResponse<String> response = new ApiResponse<>(statusCode, accessToken, null);

                return ResponseEntity.status(statusCode).body(response);
            }

            List<String> data = service.getMessageTypes();
            logger.info("Successfully fetched {} MessageTypes.", data.size());

            ObjectMapper mapper = getCustomMapper();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("messageTypes", data);
            responseData.put("accessToken", accessToken);

            String jsonResponse = mapper.writeValueAsString(responseData);
            String encryptedResponse = AESUtil.encrypt(jsonResponse, secretKey, viKey);

            return ResponseEntity.ok(new ApiResponse<>(ErrorCode.SUCCESS, encryptedResponse));

        } catch (Exception e) {
            logger.error("Error fetching Message Type data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        }
    }

    @PostMapping("/decryptMessageTypes")
    public ResponseEntity<?> decryptResponse(@RequestBody EncryptedtPayloadRequest request) {
        logger.info("Request received to decrypt data.");
        try {
            String decryptedJson = AESUtil.decrypt(request.getEncryptedPayload(), secretKey, viKey);
            ObjectMapper mapper = getCustomMapper();
            Object json = mapper.readValue(decryptedJson, Object.class);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            logger.error("Error decrypting data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ErrorCode.DECRYPTION_FAILED));
        }
    }

    @GetMapping
    public String hello() {
        logger.info("Health check called at root endpoint.");
        return "Hey Developer! I am SMSA Search api, My Deployment Successful";
    }

    @PostMapping("/encryptFilter")
    public ResponseEntity<ApiResponse<String>> encryptFilterPayload(@RequestBody FilterRequest filter) {
        try {
            logger.info("Received request to /encryptFilter: {}", filter);

            ObjectMapper mapper = getCustomMapper();
            String json = mapper.writeValueAsString(filter);
            String encrypted = AESUtil.encrypt(json, secretKey, viKey);

            return ResponseEntity.ok(new ApiResponse<>(ErrorCode.SUCCESS, encrypted));
        } catch (Exception e) {
            logger.error("Encryption failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        }
    }

    @PostMapping("/decryptFilter")
    public ResponseEntity<ApiResponse<FilterRequest>> decryptFilterPayload(@RequestBody EncryptedtPayloadRequest request) {
        try {
            logger.info("Received request to /decryptFilter");

            String decryptedJson = AESUtil.decrypt(request.getEncryptedPayload(), secretKey, viKey);
            ObjectMapper mapper = getCustomMapper();
            FilterRequest filter = mapper.readValue(decryptedJson, FilterRequest.class);

            return ResponseEntity.ok(new ApiResponse<>(ErrorCode.SUCCESS, filter));
        } catch (Exception e) {
            logger.error("Decryption failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        }
    }

    @PostMapping("/decryptSearchFilter")
    public ResponseEntity<ApiResponse<EncryptedResponseData>> decryptSearchFilterPayload(@RequestBody EncryptedtPayloadRequest request) {
        try {
            logger.info("Received request to /decryptFilter");

            String decryptedJson = AESUtil.decrypt(request.getEncryptedPayload(), secretKey, viKey);
            ObjectMapper mapper = getCustomMapper();
            EncryptedResponseData filter = mapper.readValue(decryptedJson, EncryptedResponseData.class);

            return ResponseEntity.ok(new ApiResponse<>(ErrorCode.SUCCESS, filter));
        } catch (Exception e) {
            logger.error("Decryption failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        }
    }

    public Map<String, String> encryptTOken(FilterRequest filter) {
        Map<String, String> refreshCall = new HashMap<>();
        try {
            String encryptedToken = AESUtil.encrypt(filter.getTokenRequest().get("token"), secretKey, viKey);
            refreshCall.put("token", encryptedToken);
            refreshCall.put("DeviceHash", filter.getTokenRequest().get("DeviceHash"));
        } catch (Exception e) {
            return new HashMap<>();
        }
        return refreshCall;
    }

    @PostMapping("/getSenderBicData")
    public ResponseEntity<?> getSenderBicData() {
        try {
            logger.info("Received request for getSenderBicData");
            Object senderBicData = service.getSenderBicData();

            if (senderBicData == null || ((List<?>) senderBicData).isEmpty()) {
                logger.info("No sender BIC data found.");
                return ResponseEntity.noContent().build();
            }

            logger.info("Sender BIC data fetched successfully.");
            return ResponseEntity.ok(senderBicData);
        } catch (Exception e) {
            logger.error("Exception in getSenderBicData(): ", e);
            return ResponseEntity.badRequest().body("Error fetching sender BIC data.");
        }
    }

    @PostMapping("/getReciverBicData")
    public ResponseEntity<?> getReciverBicData() {
        try {
            logger.info("Received request for getReciverBicData");
            Object receiverBicData = service.getReceiverrBicData();

            if (receiverBicData == null || ((List<?>) receiverBicData).isEmpty()) {
                logger.info("No receiver BIC data found.");
                return ResponseEntity.noContent().build();
            }

            logger.info("Receiver BIC data fetched successfully.");
            return ResponseEntity.ok(receiverBicData);
        } catch (Exception e) {
            logger.error("Exception in getReciverBicData(): ", e);
            return ResponseEntity.badRequest().body("Error fetching receiver BIC data.");
        }
    }
}
