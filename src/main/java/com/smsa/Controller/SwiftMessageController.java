package com.smsa.Controller;

import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.Enums.ErrorCode;
import com.smsa.ResponseWrappers.ApiResponse;
import com.smsa.Service.SwiftMessageService;
import com.smsa.tokenValidation.AuthenticateAPi;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping
public class SwiftMessageController {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SwiftMessageController.class);

    @Autowired
    private SwiftMessageService service;
    @Autowired
    private AuthenticateAPi authenticateApi;

    @PostMapping("/searchApi")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFilteredMessages(
            @RequestBody FilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Received request to /searchApi with filter: {}, page: {}, size: {}", filter, page, size);

        try {

            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenRequest());
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(ErrorCode.TOKEN_INVALID));
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<SwiftMessageHeaderPojo> pagedResult = service.getFilteredMessages(filter.getFilter(), pageable);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("accessToken", accessToken);
            responseData.put("messages", pagedResult.getContent());
            responseData.put("totalPages", pagedResult.getTotalPages());
            responseData.put("totalElements", pagedResult.getTotalElements());
            responseData.put("currentPage", pagedResult.getNumber());


            return ResponseEntity.ok(new ApiResponse<>(ErrorCode.SUCCESS, responseData));

        } catch (Exception ex) {
            logger.error("Unexpected error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(ErrorCode.INTERNAL_ERROR));
        }
    }

    @GetMapping("/getRecentTransactions")
    public ResponseEntity<?> getFullData(@RequestParam Map<String, String> token) {
        logger.info("Request received to fetch get recent  SMSA data.");
        try {
            String accessToken = authenticateApi.validateAndRefreshToken(token);
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(ErrorCode.TOKEN_INVALID));
            }
            List<SwiftMessageHeaderPojo> data = service.getFullData();
            logger.info("Successfully fetched {} SMSA records.", data.size());
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            logger.error("Error fetching SMSA data: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred while fetching SMSA data.");
        }
    }

    @GetMapping
    public String hello() {
        logger.info("Health check called at root endpoint.");
        return "Hey Developer! I am SMSA Search api,My Deployment Successful";
    }

    @GetMapping("/totalRecords")
    public List<SwiftMessageHeaderPojo> totalData() {
        return service.getTotalData();
    }
}
