/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.ResponseWrappers;

/**
 *
 * @author abcom
 */
import com.smsa.Enums.ErrorCode;

public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public ApiResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getCode();
        this.message = errorCode.getDefaultMessage();
    }

    public ApiResponse(ErrorCode errorCode, String message) {
        this.statusCode = errorCode.getCode();
        this.message = message;
    }

    public ApiResponse(ErrorCode errorCode, T data) {
        this.statusCode = errorCode.getCode();
        this.message = errorCode.getDefaultMessage();
        this.data = data;
    }

    public ApiResponse(ErrorCode errorCode, String message, T data) {
        this.statusCode = errorCode.getCode();
        this.message = message;
        this.data = data;
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
