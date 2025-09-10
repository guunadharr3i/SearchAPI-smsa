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
public class SmsaDownloadResponsePojo {

    private Long messageId;
    private String inpOut;
    private String senderBic;
    private String receiverBic;
    private String msgType;
    private String transactionRef;
    private String transactionRelatedRefNo;
    private LocalDate fileDate;
    private String fileTime;
    private String currency;
    private String transactionAmount;
    private String miorRef;
    private LocalDate valueDate;
    private String mText;
    private String mHistory;
    private String fileType;
    private String sendRecDateTime;
    private String unit;

    public SmsaDownloadResponsePojo(Long messageId,String inpOut, String senderBic, String receiverBic, String msgType, String transactionRef, String transactionRelatedRefNo,
            LocalDate fileDate, String fileTime, String currency, String transactionAmount, String miorRef,String fileType) {
        this.messageId=messageId;
        this.inpOut=inpOut;
        this.senderBic=senderBic;
        this.receiverBic=receiverBic;
        this.msgType=msgType;
        this.transactionRef=transactionRef;
        this.transactionRelatedRefNo=transactionRelatedRefNo;
        this.fileDate=fileDate;
        this.fileTime=fileTime;
        this.currency=currency;
        this.transactionAmount=transactionAmount;
        this.miorRef=miorRef;
        this.fileType=fileType;
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
     * @return the fileTime
     */
    public String getFileTime() {
        return fileTime;
    }

    /**
     * @param fileTime the fileTime to set
     */
    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
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
     * @return the miorRef
     */
    public String getMiorRef() {
        return miorRef;
    }

    /**
     * @param miorRef the miorRef to set
     */
    public void setMiorRef(String miorRef) {
        this.miorRef = miorRef;
    }

    /**
     * @return the valueDate
     */
    public LocalDate getValueDate() {
        return valueDate;
    }

    /**
     * @param valueDate the valueDate to set
     */
    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    /**
     * @return the mText
     */
    public String getmText() {
        return mText;
    }

    /**
     * @param mText the mText to set
     */
    public void setmText(String mText) {
        this.mText = mText;
    }

    /**
     * @return the mHistory
     */
    public String getmHistory() {
        return mHistory;
    }

    /**
     * @param mHistory the mHistory to set
     */
    public void setmHistory(String mHistory) {
        this.mHistory = mHistory;
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
     * @return the sendRecDateTime
     */
    public String getSendRecDateTime() {
        return sendRecDateTime;
    }

    /**
     * @param sendRecDateTime the sendRecDateTime to set
     */
    public void setSendRecDateTime(String sendRecDateTime) {
        this.sendRecDateTime = sendRecDateTime;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
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

}
