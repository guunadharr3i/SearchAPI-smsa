package com.smsa.Controller;

import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.Service.SwiftMessageService;
import com.smsa.entity.SwiftMessageHeader;
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
    public ResponseEntity<?> getFilteredMessages(
            @RequestBody FilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Received request to /searchApi with filter: {}, page: {}, size: {}", filter, page, size);

        try {
            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenRequest());
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token refresh failed.");
            }

            Pageable pageable = PageRequest.of(page, size);
            Page<SwiftMessageHeaderPojo> pagedResult = service.getFilteredMessages(filter.getFilter(), pageable);

            logger.info("Filtered messages retrieved successfully, count: {}", pagedResult.getTotalElements());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken);
            responseBody.put("messages", pagedResult.getContent());
            responseBody.put("totalPages", pagedResult.getTotalPages());
            responseBody.put("totalElements", pagedResult.getTotalElements());
            responseBody.put("currentPage", pagedResult.getNumber());

            return ResponseEntity.ok(responseBody);

        } catch (Exception ex) {
            logger.error("Unexpected error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred.");
        }
    }

    @GetMapping("/getSmsaData")
    public ResponseEntity<?> getFullData() {
        logger.info("Request received to fetch full SMSA data.");
        try {
            List<SwiftMessageHeader> data = service.getFullData();
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
}
