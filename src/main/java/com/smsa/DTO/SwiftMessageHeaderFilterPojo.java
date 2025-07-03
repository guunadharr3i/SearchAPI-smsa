package com.smsa.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SwiftMessageHeaderFilterPojo {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Long> messageId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> fileName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<LocalDateTime> date;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> time;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> mtCode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> page;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> fileType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> priority;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> inputRefNo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> outputRefNo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> inpOut;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> msgDesc;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> msgType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> slaId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> senderBic;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> senderBicDesc;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> receiverBic;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> receiverBicDesc;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> userRef;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> transactionRef;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<LocalDate> fileDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> mur;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> uetr;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<BigDecimal> transactionAmount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> transactionResult;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> primaryFormat;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> secondaryFormat;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> currency;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dateFrom;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate dateTo;

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
     * @return the messageId
     */
    public List<Long> getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(List<Long> messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the fileName
     */
    public List<String> getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the date
     */
    public List<LocalDateTime> getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(List<LocalDateTime> date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public List<String> getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(List<String> time) {
        this.time = time;
    }

    /**
     * @return the mtCode
     */
    public List<Integer> getMtCode() {
        return mtCode;
    }

    /**
     * @param mtCode the mtCode to set
     */
    public void setMtCode(List<Integer> mtCode) {
        this.mtCode = mtCode;
    }

    /**
     * @return the page
     */
    public List<Integer> getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(List<Integer> page) {
        this.page = page;
    }

    /**
     * @return the fileType
     */
    public List<String> getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(List<String> fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the priority
     */
    public List<String> getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(List<String> priority) {
        this.priority = priority;
    }

    /**
     * @return the inputRefNo
     */
    public List<String> getInputRefNo() {
        return inputRefNo;
    }

    /**
     * @param inputRefNo the inputRefNo to set
     */
    public void setInputRefNo(List<String> inputRefNo) {
        this.inputRefNo = inputRefNo;
    }

    /**
     * @return the outputRefNo
     */
    public List<String> getOutputRefNo() {
        return outputRefNo;
    }

    /**
     * @param outputRefNo the outputRefNo to set
     */
    public void setOutputRefNo(List<String> outputRefNo) {
        this.outputRefNo = outputRefNo;
    }

    /**
     * @return the inpOut
     */
    public List<String> getInpOut() {
        return inpOut;
    }

    /**
     * @param inpOut the inpOut to set
     */
    public void setInpOut(List<String> inpOut) {
        this.inpOut = inpOut;
    }

    /**
     * @return the msgDesc
     */
    public List<String> getMsgDesc() {
        return msgDesc;
    }

    /**
     * @param msgDesc the msgDesc to set
     */
    public void setMsgDesc(List<String> msgDesc) {
        this.msgDesc = msgDesc;
    }

    /**
     * @return the msgType
     */
    public List<String> getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(List<String> msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the slaId
     */
    public List<String> getSlaId() {
        return slaId;
    }

    /**
     * @param slaId the slaId to set
     */
    public void setSlaId(List<String> slaId) {
        this.slaId = slaId;
    }

    /**
     * @return the senderBic
     */
    public List<String> getSenderBic() {
        return senderBic;
    }

    /**
     * @param senderBic the senderBic to set
     */
    public void setSenderBic(List<String> senderBic) {
        this.senderBic = senderBic;
    }

    /**
     * @return the senderBicDesc
     */
    public List<String> getSenderBicDesc() {
        return senderBicDesc;
    }

    /**
     * @param senderBicDesc the senderBicDesc to set
     */
    public void setSenderBicDesc(List<String> senderBicDesc) {
        this.senderBicDesc = senderBicDesc;
    }

    /**
     * @return the receiverBic
     */
    public List<String> getReceiverBic() {
        return receiverBic;
    }

    /**
     * @param receiverBic the receiverBic to set
     */
    public void setReceiverBic(List<String> receiverBic) {
        this.receiverBic = receiverBic;
    }

    /**
     * @return the receiverBicDesc
     */
    public List<String> getReceiverBicDesc() {
        return receiverBicDesc;
    }

    /**
     * @param receiverBicDesc the receiverBicDesc to set
     */
    public void setReceiverBicDesc(List<String> receiverBicDesc) {
        this.receiverBicDesc = receiverBicDesc;
    }

    /**
     * @return the userRef
     */
    public List<String> getUserRef() {
        return userRef;
    }

    /**
     * @param userRef the userRef to set
     */
    public void setUserRef(List<String> userRef) {
        this.userRef = userRef;
    }

    /**
     * @return the transactionRef
     */
    public List<String> getTransactionRef() {
        return transactionRef;
    }

    /**
     * @param transactionRef the transactionRef to set
     */
    public void setTransactionRef(List<String> transactionRef) {
        this.transactionRef = transactionRef;
    }

    /**
     * @return the fileDate
     */
    public List<LocalDate> getFileDate() {
        return fileDate;
    }

    /**
     * @param fileDate the fileDate to set
     */
    public void setFileDate(List<LocalDate> fileDate) {
        this.fileDate = fileDate;
    }

    /**
     * @return the mur
     */
    public List<String> getMur() {
        return mur;
    }

    /**
     * @param mur the mur to set
     */
    public void setMur(List<String> mur) {
        this.mur = mur;
    }

    /**
     * @return the uetr
     */
    public List<String> getUetr() {
        return uetr;
    }

    /**
     * @param uetr the uetr to set
     */
    public void setUetr(List<String> uetr) {
        this.uetr = uetr;
    }

    /**
     * @return the transactionAmount
     */
    public List<BigDecimal> getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(List<BigDecimal> transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * @return the transactionResult
     */
    public List<String> getTransactionResult() {
        return transactionResult;
    }

    /**
     * @param transactionResult the transactionResult to set
     */
    public void setTransactionResult(List<String> transactionResult) {
        this.transactionResult = transactionResult;
    }

    /**
     * @return the primaryFormat
     */
    public List<String> getPrimaryFormat() {
        return primaryFormat;
    }

    /**
     * @param primaryFormat the primaryFormat to set
     */
    public void setPrimaryFormat(List<String> primaryFormat) {
        this.primaryFormat = primaryFormat;
    }

    /**
     * @return the secondaryFormat
     */
    public List<String> getSecondaryFormat() {
        return secondaryFormat;
    }

    /**
     * @param secondaryFormat the secondaryFormat to set
     */
    public void setSecondaryFormat(List<String> secondaryFormat) {
        this.secondaryFormat = secondaryFormat;
    }

    /**
     * @return the currency
     */
    public List<String> getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

}
