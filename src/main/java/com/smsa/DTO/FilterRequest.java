/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

import com.smsa.DTO.SwiftRequestPojo;
import java.util.Map;

/**
 *
 * @author dell
 */
public class FilterRequest {
    private Map<String,String> tokenRequest;
    private SwiftRequestPojo filter;

    /**
     * @return the tokenRequest
     */
    public Map<String,String> getTokenRequest() {
        return tokenRequest;
    }

    /**
     * @param tokenRequest the tokenRequest to set
     */
    public void setTokenRequest(Map<String,String> tokenRequest) {
        this.tokenRequest = tokenRequest;
    }

    /**
     * @return the filter
     */
    public SwiftRequestPojo getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(SwiftRequestPojo filter) {
        this.filter = filter;
    }
    
}
