/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.smsa.Enums;

/**
 *
 * @author abcom
 */
public enum ApiResponseCode {
    SUCCESS(1000, "Success"),
    INVALID_TOKEN(1001, "Token validation failed"),
    INVALID_DOWNLOAD_TYPE(1002, "Invalid download type"),
    FILE_NOT_FOUND(1003, "Requested file not found"),
    NO_RECORDS(1004, "No records found to export"),
    INTERNAL_ERROR(1005, "Internal server error"),
    FILE_ERROR(1006, "Exception Occurred");
    private final int code;
    private final String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
