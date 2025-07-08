/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.CustomExceptions;

/**
 *
 * @author abcom
 */
public class CountryDataLoadException extends RuntimeException {

    public CountryDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
