/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

import java.util.List;

/**
 *
 * @author abcom
 */
public class AuthRequest {

    private String token;

    private String deviceHash;
    private List<String> geoIds;
    private String moduleName;

    public AuthRequest() {
    }

    public AuthRequest(String token, String deviceHash) {
        this.token = token;
        this.deviceHash = deviceHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceHash() {
        return deviceHash;
    }

    public void setDeviceHash(String deviceHash) {
        this.deviceHash = deviceHash;
    }

    /**
     * @return the geoIds
     */
    public List<String> getGeoIds() {
        return geoIds;
    }

    /**
     * @param geoIds the geoIds to set
     */
    public void setGeoIds(List<String> geoIds) {
        this.geoIds = geoIds;
    }

    /**
     * @return the moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
