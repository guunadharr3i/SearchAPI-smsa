package com.smsa.Controller;

import com.smsa.Service.SwiftMessageService;
import com.smsa.entity.SwiftMessageHeader;
import com.smsa.DTO.FilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping
public class SwiftMessageController {

    private static final Logger logger = LoggerFactory.getLogger(SwiftMessageController.class);

    @Autowired
    private SwiftMessageService service;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${authentication.url}")
    private String authenticationUrl;

    @PostMapping("/searchApi")
    public ResponseEntity<?> getFilteredMessages(
            @RequestBody FilterRequest filter) {

        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(filter.getTokenRequest(), headers);

            // Call /refresh-token endpoint
            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                    authenticationUrl, requestEntity, String.class);

            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                try {
                    // Proceed with actual service call
                    List<SwiftMessageHeader> filteredMessages = service.getFilteredMessages(filter.getFilter());
                    return ResponseEntity.ok(filteredMessages);

                } catch (Exception ex) {
                    logger.error("Error while retrieving filtered messages: {}", ex.getMessage(), ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error retrieving messages.");
                }
            } else {
                logger.error("Token refresh failed: HTTP {}", tokenResponse.getStatusCodeValue());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token refresh failed.");
            }

        } catch (HttpClientErrorException ex) {
            logger.error("HTTP error during token refresh: {}", ex.getStatusCode());
            return ResponseEntity.status(ex.getStatusCode())
                    .body("Token refresh error: " + ex.getMessage());

        } catch (RestClientException ex) {
            logger.error("RestClientException during token refresh: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calling token refresh service.");

        } catch (Exception ex) {
            logger.error("Unexpected error during token refresh: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred.");
        }

    }

    @GetMapping
    public String hello() {
        return "Deployment Successful";
    }
}
