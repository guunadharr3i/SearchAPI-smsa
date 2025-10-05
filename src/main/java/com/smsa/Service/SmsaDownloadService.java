/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;

import com.smsa.DTO.SmsaDownloadResponsePojo;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import java.util.List;

/**
 *
 * @author abcom
 */
public interface SmsaDownloadService {
    
    public List<SmsaDownloadResponsePojo> filterDownloadData(SwiftMessageHeaderFilterPojo filter);
    
}
