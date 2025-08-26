/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.tokenValidation;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author abcom
 */
@Service
public class AuthenticateAPi {

    private static final Logger logger = LogManager.getLogger(AuthenticateAPi.class);

    @Value("${authentication.url}")
    private String authenticationUrl;

    @Autowired
    private RestTemplate restTemplate;  // ✅ Inject pooled RestTemplate

    @Autowired
    private ObjectMapper objectMapper;

    public String validateAndRefreshToken(Map<String, String> tokenRequest) {
        int maxRetries = 5;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(tokenRequest, headers);

                logger.info("Attempt {} - Calling authentication service at {}", attempt, new Date());
                ResponseEntity<String> tokenResponse =
                        restTemplate.postForEntity(authenticationUrl, requestEntity, String.class);

                logger.info("Response received, status: {}", tokenResponse.getStatusCodeValue());

                if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                    String tokenJson = tokenResponse.getBody();
                    String accessToken = objectMapper.readTree(tokenJson).get("accessToken").asText();
                    return tokenResponse.getStatusCodeValue() + ":" + accessToken;
                } else {
                    logger.warn("Non-2xx response on attempt {}: {}", attempt, tokenResponse.getStatusCodeValue());
                    return tokenResponse.getStatusCodeValue() + ":Token refresh failed";
                }

            } catch (Exception ex) {
                logger.error("Error during token refresh (attempt {}): {}", attempt, ex.getMessage());
            }

            // Retry with exponential backoff
            if (attempt < maxRetries) {
                long backoff = attempt * 1000L; // 1s, 2s, 3s...
                logger.info("Retrying in {} ms (attempt {} of {})", backoff, attempt + 1, maxRetries);
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return "500:Retry interrupted";
                }
            }
        }

        return "503:All retry attempts failed for token refresh";
    }
}
