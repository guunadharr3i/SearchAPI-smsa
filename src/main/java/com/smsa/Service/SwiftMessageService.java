/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author abcom
 */
package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.entity.SwiftMessageHeader;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SwiftMessageService {
//    SwiftRequestPojo getSwiftMessageByTxnRef(String txnRef);

    public Page<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderPojo filters,Pageable pageable);
    
    public List<SwiftMessageHeader> getFullData();
 
}
