/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author abcom
 */
public class SwiftMessageHeaderPojo {

    private Long messageId;
    private String fileName;
    private LocalDateTime date;
    private String time;
    private Integer mtCode;
    private Integer page;
    private String fileType;
    private String priority;
    private String inputRefNo;
    private String outputRefNo;
    private String inpOut;
    private String msgDesc;
    private String msgType;
    private String slaId;
    private String senderBic;
    private String senderBicDesc;
    private String receiverBic;
    private String receiverBicDesc;
    private String userRef;
    private String transactionRef;
    private LocalDate fileDate;
    private String mur;
    private String uetr;
    private BigDecimal transactionAmount;
    private String transactionResult;
    private String primaryFormat;
    private String secondaryFormat;
    private String currency;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rawTxt;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dateFrom;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dateTo;

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
     * @return the date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the mtCode
     */
    public Integer getMtCode() {
        return mtCode;
    }

    /**
     * @param mtCode the mtCode to set
     */
    public void setMtCode(Integer mtCode) {
        this.mtCode = mtCode;
    }

    /**
     * @return the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
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
     * @return the inputRefNo
     */
    public String getInputRefNo() {
        return inputRefNo;
    }

    /**
     * @param inputRefNo the inputRefNo to set
     */
    public void setInputRefNo(String inputRefNo) {
        this.inputRefNo = inputRefNo;
    }

    /**
     * @return the outputRefNo
     */
    public String getOutputRefNo() {
        return outputRefNo;
    }

    /**
     * @param outputRefNo the outputRefNo to set
     */
    public void setOutputRefNo(String outputRefNo) {
        this.outputRefNo = outputRefNo;
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
     * @return the msgDesc
     */
    public String getMsgDesc() {
        return msgDesc;
    }

    /**
     * @param msgDesc the msgDesc to set
     */
    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
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
     * @return the slaId
     */
    public String getSlaId() {
        return slaId;
    }

    /**
     * @param slaId the slaId to set
     */
    public void setSlaId(String slaId) {
        this.slaId = slaId;
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
     * @return the senderBicDesc
     */
    public String getSenderBicDesc() {
        return senderBicDesc;
    }

    /**
     * @param senderBicDesc the senderBicDesc to set
     */
    public void setSenderBicDesc(String senderBicDesc) {
        this.senderBicDesc = senderBicDesc;
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
     * @return the receiverBicDesc
     */
    public String getReceiverBicDesc() {
        return receiverBicDesc;
    }

    /**
     * @param receiverBicDesc the receiverBicDesc to set
     */
    public void setReceiverBicDesc(String receiverBicDesc) {
        this.receiverBicDesc = receiverBicDesc;
    }

    /**
     * @return the userRef
     */
    public String getUserRef() {
        return userRef;
    }

    /**
     * @param userRef the userRef to set
     */
    public void setUserRef(String userRef) {
        this.userRef = userRef;
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
     * @return the mur
     */
    public String getMur() {
        return mur;
    }

    /**
     * @param mur the mur to set
     */
    public void setMur(String mur) {
        this.mur = mur;
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
     * @return the dateFrom
     */
    public LocalDate getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public LocalDate getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the transactionAmount
     */
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the transactionResult
     */
    public String getTransactionResult() {
        return transactionResult;
    }

    /**
     * @param transactionResult the transactionResult to set
     */
    public void setTransactionResult(String transactionResult) {
        this.transactionResult = transactionResult;
    }

    /**
     * @return the primaryFormat
     */
    public String getPrimaryFormat() {
        return primaryFormat;
    }

    /**
     * @param primaryFormat the primaryFormat to set
     */
    public void setPrimaryFormat(String primaryFormat) {
        this.primaryFormat = primaryFormat;
    }

    /**
     * @return the secondaryFormat
     */
    public String getSecondaryFormat() {
        return secondaryFormat;
    }

    /**
     * @param secondaryFormat the secondaryFormat to set
     */
    public void setSecondaryFormat(String secondaryFormat) {
        this.secondaryFormat = secondaryFormat;
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
