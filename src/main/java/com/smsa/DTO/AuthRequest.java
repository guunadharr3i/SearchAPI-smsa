/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

/**
 *
 * @author abcom
 */

public class AuthRequest {

    private String token;

    private String deviceHash;

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
}
