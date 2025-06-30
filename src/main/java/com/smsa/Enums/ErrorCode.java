/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.smsa.Enums;

/**
 *
 * @author abcom
 */
public enum ErrorCode {
    SUCCESS(200, "Operation successful"),
    TOKEN_INVALID(401, "Token validation failed"),
    DATA_NOT_FOUND(404, "Requested data not found"),
    VALIDATION_ERROR(400, "Invalid input data"),
    INTERNAL_ERROR(500, "Internal server error"),
    UNAUTHORIZED(403, "You are not authorized to access this resource");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
