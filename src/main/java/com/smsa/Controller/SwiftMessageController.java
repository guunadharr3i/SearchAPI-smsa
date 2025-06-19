package com.smsa.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.Service.SwiftMessageService;
import com.smsa.entity.SwiftMessageHeader;
import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${authentication.url}")
    private String authenticationUrl;

    @Autowired
    private ObjectMapper objectMapper; // Add this if not autowired already

    @PostMapping("/searchApi")
public ResponseEntity<?> getFilteredMessages(
        @RequestBody FilterRequest filter,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    logger.info("Received request to /searchApi with filter: {}, page: {}, size: {}", filter, page, size);

    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(filter.getTokenRequest(), headers);

        logger.debug("Calling authentication service at URL: {}", authenticationUrl);
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(authenticationUrl, requestEntity, String.class);

        if (tokenResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Token refresh successful, status: {}", tokenResponse.getStatusCodeValue());

            String tokenJson = tokenResponse.getBody();
            String accessToken = objectMapper.readTree(tokenJson).get("accessToken").asText();

            try {
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
                logger.error("Error retrieving filtered messages: {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error retrieving messages.");
            }

        } else {
            logger.warn("Token refresh failed, status code: {}", tokenResponse.getStatusCodeValue());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token refresh failed.");
        }

    } catch (HttpClientErrorException ex) {
        logger.error("HTTP client error: {}, response body: {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getStatusCode())
                .body("Token refresh error: " + ex.getMessage());

    } catch (RestClientException ex) {
        logger.error("RestClientException during token refresh: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error calling token refresh service.");

    } catch (Exception ex) {
        logger.error("Unexpected error during token refresh: {}", ex.getMessage(), ex);
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
        return "Deployment Successful";
    }
}
