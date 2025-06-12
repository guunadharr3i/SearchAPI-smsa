/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author abcom
 */
package com.smsa.Service;

import com.smsa.DTO.SwiftRequestPojo;
import com.smsa.entity.SwiftMessageHeader;
import java.util.List;

public interface SwiftMessageService {
//    SwiftRequestPojo getSwiftMessageByTxnRef(String txnRef);

    public List<SwiftMessageHeader> getFilteredMessages(SwiftRequestPojo filters);
}
