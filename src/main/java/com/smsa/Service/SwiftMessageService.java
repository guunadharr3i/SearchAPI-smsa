/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author abcom
 */
package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SwiftMessageService {

    public Page<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderFilterPojo filters, Pageable pageable);

    public List<SwiftMessageHeaderPojo> getFullData();

    public List<SwiftMessageHeaderPojo> getFilteredMessages(SwiftMessageHeaderFilterPojo filters);

    public List<SwiftMessageHeaderPojo> getTotalData();

    public List<String> getMessageTypes();
    
    public List<String> getRawData(String transactionRef);

}
