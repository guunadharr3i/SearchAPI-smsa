/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.ResponseWrappers;

import com.smsa.Enums.ApiResponseCode;


/**
 *
 * @author abcom
 */

public class DownloadApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public DownloadApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <T> DownloadApiResponse<T> of(ApiResponseCode code, T data) {
        return new DownloadApiResponse<>(code.getCode(), code.getMessage(), data);
    }

    public static <T> DownloadApiResponse<T> error(ApiResponseCode code) {
        return new DownloadApiResponse<>(code.getCode(), code.getMessage(), null);
    }

    // Getters and setters

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
}

