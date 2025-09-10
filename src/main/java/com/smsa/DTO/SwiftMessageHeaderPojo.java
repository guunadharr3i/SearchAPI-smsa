/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

import java.time.LocalDate;

/**
 *
 * @author abcom
 */
public class SwiftMessageHeaderPojo {

    private Long messageId;
    private String senderBic;
    private String receiverBic;
    private String currency;
    private String transactionAmount;
    private String inpOut;
    private String uetr;
    private LocalDate fileDate;
    private String fileType;
    private String msgType;
    private String transactionRef;
    private String fileName;
    private String transactionRelatedRefNo;
    private String rawTxt;
    

    public SwiftMessageHeaderPojo() {
    }

    // ✅ Constructor for JPQL
    public SwiftMessageHeaderPojo(
            Long messageId,
            String senderBic,
            String receiverBic,
            String currency,
            String transactionAmount,
            String inpOut,
            String uetr,
            LocalDate fileDate,
            String fileType,
            String msgType,
            String transactionRef,
            String fileName,
            String transactionRelatedRefNo,
            String rawTxt
            
    ) {
        this.messageId = messageId;
        this.senderBic = senderBic;
        this.receiverBic = receiverBic;
        this.currency = currency;
        this.transactionAmount = transactionAmount;
        this.inpOut = inpOut;
        this.uetr = uetr;
        this.fileDate = fileDate;
        this.fileType = fileType;
        this.msgType = msgType;
        this.transactionRef = transactionRef;
        this.fileName = fileName;
        this.rawTxt = rawTxt;
        this.transactionRelatedRefNo=transactionRelatedRefNo;
    }

    /**
     * @return the messageId
     */
    public Long getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the senderBic
     */
    public String getSenderBic() {
        return senderBic;
    }

    /**
     * @param senderBic the senderBic to set
     */
    public void setSenderBic(String senderBic) {
        this.senderBic = senderBic;
    }

    /**
     * @return the receiverBic
     */
    public String getReceiverBic() {
        return receiverBic;
    }

    /**
     * @param receiverBic the receiverBic to set
     */
    public void setReceiverBic(String receiverBic) {
        this.receiverBic = receiverBic;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the transactionAmount
     */
    public String getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the inpOut
     */
    public String getInpOut() {
        return inpOut;
    }

    /**
     * @param inpOut the inpOut to set
     */
    public void setInpOut(String inpOut) {
        this.inpOut = inpOut;
    }

    /**
     * @return the uetr
     */
    public String getUetr() {
        return uetr;
    }

    /**
     * @param uetr the uetr to set
     */
    public void setUetr(String uetr) {
        this.uetr = uetr;
    }

    /**
     * @return the fileDate
     */
    public LocalDate getFileDate() {
        return fileDate;
    }

    /**
     * @param fileDate the fileDate to set
     */
    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the transactionRef
     */
    public String getTransactionRef() {
        return transactionRef;
    }

    /**
     * @param transactionRef the transactionRef to set
     */
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

 

    /**
     * @return the transactionRelatedRefNo
     */
    public String getTransactionRelatedRefNo() {
        return transactionRelatedRefNo;
    }

    /**
     * @param transactionRelatedRefNo the transactionRelatedRefNo to set
     */
    public void setTransactionRelatedRefNo(String transactionRelatedRefNo) {
        this.transactionRelatedRefNo = transactionRelatedRefNo;
    }

    /**
     * @return the rawTxt
     */
    public String getRawTxt() {
        return rawTxt;
    }

    /**
     * @param rawTxt the rawTxt to set
     */
    public void setRawTxt(String rawTxt) {
        this.rawTxt = rawTxt;
    }

}
