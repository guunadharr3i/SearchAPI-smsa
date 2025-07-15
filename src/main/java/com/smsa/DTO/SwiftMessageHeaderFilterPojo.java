package com.smsa.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SwiftMessageHeaderFilterPojo {

    private List<Long> messageId;
    private List<String> fileName;
    private List<LocalDateTime> date;
    private List<String> time;
    private List<Integer> mtCode;
    private List<Integer> page;
    private List<String> fileType;
    private List<String> priority;
    private List<String> inputRefNo;
    private List<String> outputRefNo;
    private List<String> inpOut;
    private List<String> msgDesc;
    private List<String> msgType;
    private List<String> slaId;
    private List<String> senderBic;
    private List<String> senderBicDesc;
    private List<String> receiverBic;
    private List<String> receiverBicDesc;
    private List<String> userRef;
    private List<String> transactionRef;
    private List<LocalDate> fileDate;
    private List<String> mur;
    private List<String> uetr;
    private List<BigDecimal> transactionAmount;
    private List<String> transactionResult;
    private List<String> primaryFormat;
    private List<String> secondaryFormat;
    private List<String> currency;
    private LocalDate dateFrom;
    private LocalDate dateTo;
     private List<String> columnSort;

    // Getters and Setters
    public List<Long> getMessageId() {
        return messageId;
    }

    public void setMessageId(List<Long> messageId) {
        this.messageId = messageId;
    }

    public List<String> getFileName() {
        return fileName;
    }

    public void setFileName(List<String> fileName) {
        this.fileName = fileName;
    }

    public List<LocalDateTime> getDate() {
        return date;
    }

    public void setDate(List<LocalDateTime> date) {
        this.date = date;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Integer> getMtCode() {
        return mtCode;
    }

    public void setMtCode(List<Integer> mtCode) {
        this.mtCode = mtCode;
    }

    public List<Integer> getPage() {
        return page;
    }

    public void setPage(List<Integer> page) {
        this.page = page;
    }

    public List<String> getFileType() {
        return fileType;
    }

    public void setFileType(List<String> fileType) {
        this.fileType = fileType;
    }

    public List<String> getPriority() {
        return priority;
    }

    public void setPriority(List<String> priority) {
        this.priority = priority;
    }

    public List<String> getInputRefNo() {
        return inputRefNo;
    }

    public void setInputRefNo(List<String> inputRefNo) {
        this.inputRefNo = inputRefNo;
    }

    public List<String> getOutputRefNo() {
        return outputRefNo;
    }

    public void setOutputRefNo(List<String> outputRefNo) {
        this.outputRefNo = outputRefNo;
    }

    public List<String> getInpOut() {
        return inpOut;
    }

    public void setInpOut(List<String> inpOut) {
        this.inpOut = inpOut;
    }

    public List<String> getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(List<String> msgDesc) {
        this.msgDesc = msgDesc;
    }

    public List<String> getMsgType() {
        return msgType;
    }

    public void setMsgType(List<String> msgType) {
        this.msgType = msgType;
    }

    public List<String> getSlaId() {
        return slaId;
    }

    public void setSlaId(List<String> slaId) {
        this.slaId = slaId;
    }

    public List<String> getSenderBic() {
        return senderBic;
    }

    public void setSenderBic(List<String> senderBic) {
        this.senderBic = senderBic;
    }

    public List<String> getSenderBicDesc() {
        return senderBicDesc;
    }

    public void setSenderBicDesc(List<String> senderBicDesc) {
        this.senderBicDesc = senderBicDesc;
    }

    public List<String> getReceiverBic() {
        return receiverBic;
    }

    public void setReceiverBic(List<String> receiverBic) {
        this.receiverBic = receiverBic;
    }

    public List<String> getReceiverBicDesc() {
        return receiverBicDesc;
    }

    public void setReceiverBicDesc(List<String> receiverBicDesc) {
        this.receiverBicDesc = receiverBicDesc;
    }

    public List<String> getUserRef() {
        return userRef;
    }

    public void setUserRef(List<String> userRef) {
        this.userRef = userRef;
    }

    public List<String> getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(List<String> transactionRef) {
        this.transactionRef = transactionRef;
    }

    public List<LocalDate> getFileDate() {
        return fileDate;
    }

    public void setFileDate(List<LocalDate> fileDate) {
        this.fileDate = fileDate;
    }

    public List<String> getMur() {
        return mur;
    }

    public void setMur(List<String> mur) {
        this.mur = mur;
    }

    public List<String> getUetr() {
        return uetr;
    }

    public void setUetr(List<String> uetr) {
        this.uetr = uetr;
    }

    public List<BigDecimal> getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(List<BigDecimal> transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public List<String> getTransactionResult() {
        return transactionResult;
    }

    public void setTransactionResult(List<String> transactionResult) {
        this.transactionResult = transactionResult;
    }

    public List<String> getPrimaryFormat() {
        return primaryFormat;
    }

    public void setPrimaryFormat(List<String> primaryFormat) {
        this.primaryFormat = primaryFormat;
    }

    public List<String> getSecondaryFormat() {
        return secondaryFormat;
    }

    public void setSecondaryFormat(List<String> secondaryFormat) {
        this.secondaryFormat = secondaryFormat;
    }

    public List<String> getCurrency() {
        return currency;
    }

    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the columnSort
     */
    public List<String> getColumnSort() {
        return columnSort;
    }

    /**
     * @param columnSort the columnSort to set
     */
    public void setColumnSort(List<String> columnSort) {
        this.columnSort = columnSort;
    }
}
