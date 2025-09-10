package com.smsa.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SwiftMessageHeaderFilterPojo {

    private List<Long> messageId;
    private List<String> fileName;
    private String fromTime;
    private String toTime;
    private String fromAmount;
    private String toAmount;
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
    private String userRef;
    private List<String> transactionRef;
    private List<LocalDate> fileDate;
    private List<String> mur;
    private String uetr;
    private List<String> transactionResult;
    private List<String> primaryFormat;
    private List<String> secondaryFormat;
    private List<String> currency;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private List<String> columnSort = new ArrayList<>();
    private String sortType = "DESC";
    private String rawMessageData;
    private String transactionRelatedRefNo;
    private boolean withMsgText=false;

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

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
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

    public String getUetr() {
        return uetr;
    }

    public void setUetr(String uetr) {
        this.uetr = uetr;
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

    /**
     * @return the orderType
     */
    public String getSortType() {
        return sortType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setSortType(String orderType) {
        this.sortType = orderType;
    }

    /**
     * @return the rawMessageData
     */
    public String getRawMessageData() {
        return rawMessageData;
    }

    /**
     * @param rawMessageData the rawMessageData to set
     */
    public void setRawMessageData(String rawMessageData) {
        this.rawMessageData = rawMessageData;
    }

    /**
     * @return the fromTime
     */
    public String getFromTime() {
        return fromTime;
    }

    /**
     * @param fromTime the fromTime to set
     */
    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    /**
     * @return the toTime
     */
    public String getToTime() {
        return toTime;
    }

    /**
     * @param toTime the toTime to set
     */
    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    /**
     * @return the fromAmount
     */
    public String getFromAmount() {
        return fromAmount;
    }

    /**
     * @param fromAmount the fromAmount to set
     */
    public void setFromAmount(String fromAmount) {
        this.fromAmount = fromAmount;
    }

    /**
     * @return the toAmount
     */
    public String getToAmount() {
        return toAmount;
    }

    /**
     * @param toAmount the toAmount to set
     */
    public void setToAmount(String toAmount) {
        this.toAmount = toAmount;
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
     * @return the withMsgText
     */
    public boolean isWithMsgText() {
        return withMsgText;
    }

    /**
     * @param withMsgText the withMsgText to set
     */
    public void setWithMsgText(boolean withMsgText) {
        this.withMsgText = withMsgText;
    }

}
