/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

import com.smsa.entity.SwiftMessageHeader;
import java.util.List;

/**
 *
 * @author abcom
 */
public class SearchApiResponse {
    private String accessToken;
    private List<SwiftMessageHeader> filteredMessages;

    // Constructors
    public SearchApiResponse() {}

    public SearchApiResponse(String accessToken, List<SwiftMessageHeader> filteredMessages) {
        this.accessToken = accessToken;
        this.filteredMessages = filteredMessages;
    }

    // Getters and setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<SwiftMessageHeader> getFilteredMessages() {
        return filteredMessages;
    }

    public void setFilteredMessages(List<SwiftMessageHeader> filteredMessages) {
        this.filteredMessages = filteredMessages;
    }
}
