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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author abcom
 */
@Service
public class AuthenticateAPi {

    @Value("${authentication.url}")
    private String authenticationUrl;

    private static final Logger logger = LogManager.getLogger(AuthenticateAPi.class);
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ObjectMapper objectMapper; // Add this if not autowired already

    public String validateAndRefreshToken(Map<String, String> tokenRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(tokenRequest, headers);
            logger.info("Calling authentication service at URL: {}", authenticationUrl + " time: " + new Date());

            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(authenticationUrl, requestEntity, String.class);
            logger.info("time after call :" + new Date());
            logger.info("after microservice call status code " + tokenResponse.getStatusCodeValue());

            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                logger.info("Token refresh successful, status: {}", tokenResponse.getStatusCodeValue());

                String tokenJson = tokenResponse.getBody();
                return objectMapper.readTree(tokenJson).get("accessToken").asText();
            } else {
                logger.info("Token refresh failed, status code: {}", tokenResponse.getStatusCodeValue());
                return null;
            }

        } catch (HttpClientErrorException ex) {
            logger.info("HTTP client error during token refresh: {}, response: {}", ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
            return null;
        } catch (RestClientException ex) {
            logger.info("RestClientException during token refresh: {}", ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            logger.info("Unexpected error during token refresh: {}", ex.getMessage(), ex);
            return null;
        }
    }

}
