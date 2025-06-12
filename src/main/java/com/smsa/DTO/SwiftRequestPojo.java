/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

/**
 *
 * @author abcom
 */
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SwiftRequestPojo {

    // From SwiftMessageHeader
    private Long messageId;
    private String transactionRef;
    private String fileName;
    private LocalDateTime date;
    private String time;
    private Integer mtCode;
    private Integer page;
    private String rawMessageData;
    private String instanceRaw;
    private String headerRaw;
    private String priority;
    private String smsaHeaderObj;
    private String inputRefNo;
    private String outputRefNo;
    private String inpOut;
    private String msgDesc;
    private String msgType;
    private String slaId;
    private String senderBic;
    private String senderObj;
    private String senderBicDesc;
    private String receiverobj;
    private String receiverBic;
    private String receiverBicDesc;
    private String userRef;
    private LocalDate fileDate;
    private String mur;
    private String uetr;

    // From SwiftMessageText
    private String rawMessageText;
    private String messageRaw;

    // From SwiftMessageTrailer
    private String msgChk;
    private String msgSig;
    private String remark;

    // Getters and Setters (all below)
    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getMtCode() {
        return mtCode;
    }

    public void setMtCode(Integer mtCode) {
        this.mtCode = mtCode;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getRawMessageData() {
        return rawMessageData;
    }

    public void setRawMessageData(String rawMessageData) {
        this.rawMessageData = rawMessageData;
    }

    public String getInstanceRaw() {
        return instanceRaw;
    }

    public void setInstanceRaw(String instanceRaw) {
        this.instanceRaw = instanceRaw;
    }

    public String getHeaderRaw() {
        return headerRaw;
    }

    public void setHeaderRaw(String headerRaw) {
        this.headerRaw = headerRaw;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSmsaHeaderObj() {
        return smsaHeaderObj;
    }

    public void setSmsaHeaderObj(String smsaHeaderObj) {
        this.smsaHeaderObj = smsaHeaderObj;
    }

    public String getInputRefNo() {
        return inputRefNo;
    }

    public void setInputRefNo(String inputRefNo) {
        this.inputRefNo = inputRefNo;
    }

    public String getOutputRefNo() {
        return outputRefNo;
    }

    public void setOutputRefNo(String outputRefNo) {
        this.outputRefNo = outputRefNo;
    }

    public String getInpOut() {
        return inpOut;
    }

    public void setInpOut(String inpOut) {
        this.inpOut = inpOut;
    }

    public String getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSlaId() {
        return slaId;
    }

    public void setSlaId(String slaId) {
        this.slaId = slaId;
    }

    public String getSenderBic() {
        return senderBic;
    }

    public void setSenderBic(String senderBic) {
        this.senderBic = senderBic;
    }

    public String getSenderObj() {
        return senderObj;
    }

    public void setSenderObj(String senderObj) {
        this.senderObj = senderObj;
    }

    public String getSenderBicDesc() {
        return senderBicDesc;
    }

    public void setSenderBicDesc(String senderBicDesc) {
        this.senderBicDesc = senderBicDesc;
    }

    public String getReceiverobj() {
        return receiverobj;
    }

    public void setReceiverobj(String receiverobj) {
        this.receiverobj = receiverobj;
    }

    public String getReceiverBic() {
        return receiverBic;
    }

    public void setReceiverBic(String receiverBic) {
        this.receiverBic = receiverBic;
    }

    public String getReceiverBicDesc() {
        return receiverBicDesc;
    }

    public void setReceiverBicDesc(String receiverBicDesc) {
        this.receiverBicDesc = receiverBicDesc;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
    }

    public LocalDate getFileDate() {
        return fileDate;
    }

    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    public String getMur() {
        return mur;
    }

    public void setMur(String mur) {
        this.mur = mur;
    }

    public String getUetr() {
        return uetr;
    }

    public void setUetr(String uetr) {
        this.uetr = uetr;
    }

    public String getRawMessageText() {
        return rawMessageText;
    }

    public void setRawMessageText(String rawMessageText) {
        this.rawMessageText = rawMessageText;
    }

    public String getMessageRaw() {
        return messageRaw;
    }

    public void setMessageRaw(String messageRaw) {
        this.messageRaw = messageRaw;
    }

    public String getMsgChk() {
        return msgChk;
    }

    public void setMsgChk(String msgChk) {
        this.msgChk = msgChk;
    }

    public String getMsgSig() {
        return msgSig;
    }

    public void setMsgSig(String msgSig) {
        this.msgSig = msgSig;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SwiftRequestPojo{"
                + "transactionRef='" + transactionRef + '\''
                + ", senderBic='" + senderBic + '\''
                + ", fileDate=" + fileDate
                + ", mtCode=" + mtCode
                + ", msgType='" + msgType + '\''
                + '}';
    }

}
