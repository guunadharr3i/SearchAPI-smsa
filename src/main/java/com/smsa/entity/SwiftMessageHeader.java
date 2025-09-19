package com.smsa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SMSA_PRT_MESSAGE_HDR")
public class SwiftMessageHeader implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "SEQ_SMSA_MESSAGE_HDR", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID", columnDefinition = "NUMBER(19,0)")
    private Long messageId;


    public Long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getHeaderRawJson() {
        return this.headerRawJson;
    }

    public void setHeaderRawJson(String headerRawJson) {
        this.headerRawJson = headerRawJson;
    }
    /**
     * @param messageHdrId
     *                     This parameter holds the message reference number,
     *                     extracted from the first line of the message text.
     *
     *                     Example input line:
     * 
     *                     15/06/25-08:37:26 ICICMSG-3834-000001 1
     *
     *                     Extracted value:
     * 
     *                     ICICMSG-3834-000001
     *
     */

    @Column(name = "SMSA_MSG_HDRID", columnDefinition = "VARCHAR2(100)")
    private String messageHdrId;

    /**
     * This is unique for each message its combination
     * of Message input Reference Number and
     * Message output Reference Number
     * it will always has value either MIR or MOR
     */
    @Column(name = "SMSA_MIOR_REF", unique = true, columnDefinition = "VARCHAR2(100)", nullable = false)
    private String miorRef;

    @Column(name = "SMSA_FILE_NAME", columnDefinition = "VARCHAR2(100)", nullable = false)
    private String fileName;

    @Column(name = "SMSA_DATE", columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime date;
    @Column(name = "SMSA_FILE_TIME", columnDefinition = "VARCHAR2(10)")
    private String fileTime;
    @Column(name = "SMSA_MT_CODE", columnDefinition = "NUMBER(5,0)")
    private Integer mtCode;

    @Column(name = "SMSA_PAGE", columnDefinition = "NUMBER(5,0)")
    private Integer page;
    /**
     * fill raw text data of 1 message
     */
    @Lob
    @Column(name = "SMSA_RAW_DATA")
    private String rawMessageData;
    /**
     * full header raw data as json
     */
    @Column(name = "SMSA_HDR_OBJ", columnDefinition = "JSON")
    private String headerRawJson;

    /**
     * full file data as json
     */
    @Column(name = "SMSA_MSG_FULL_JSON", columnDefinition = "JSON")
    private String messageFullJson;

    @Column(name = "SMSA_FILE_TYPE", columnDefinition = "VARCHAR2(50)")
    private String fileType;

    @Lob
    @Column(name = "SMSA_HDR_TEXT")
    private String smsaHeaderText;

    @Column(name = "SMSA_GEO_ID", columnDefinition = "VARCHAR2(10)")
    private String geoId;

    @Column(name = "SMSA_MSG_IO", columnDefinition = "VARCHAR2(1)")
    private String inpOut;

    @Column(name = "SMSA_MSG_DESC", columnDefinition = "VARCHAR2(255)")
    private String msgDesc;

    @Column(name = "SMSA_MSG_TYPE", columnDefinition = "VARCHAR2(255)")
    private String msgType;

    @Column(name = "SMSA_SLA_ID", columnDefinition = "VARCHAR2(255)")
    private String slaId;

    @Column(name = "SMSA_SENDER_BIC", columnDefinition = "VARCHAR2(20)")
    private String senderBic;

    // @Column(name = "SMSA_SENDER_OBJ", columnDefinition = "JSON")
    // private String senderObj;

    // @Column(name = "SMSA_SENDBIC_JSON", columnDefinition = "JSON")
    // private String senderBicJson;

    @Column(name = "SMSA_SENDER_BIC_DESC", columnDefinition = "VARCHAR2(255)")
    private String senderBicDesc;

    // @Column(name = "SMSA_RECEIVER_OBJ", columnDefinition = "JSON")
    // private String receiverObj;

    // @Column(name = "SMSA_RECBIC_JSON", columnDefinition = "JSON")
    // private String receiverBicJson;

    @Column(name = "SMSA_RECEIVER_BIC", columnDefinition = "VARCHAR2(20)")
    private String receiverBic;

    @Column(name = "SMSA_RECEIVER_BIC_DESC", columnDefinition = "VARCHAR2(255)")
    private String receiverBicDesc;

    @Column(name = "SMSA_TXN_REF", columnDefinition = "VARCHAR2(255)")
    private String transactionRef;

    @Column(name = "SMSA_FILE_DATE", columnDefinition = "DATE")
    private LocalDate fileDate;

    @Column(name = "SMSA_MUR", columnDefinition = "VARCHAR2(255)")
    private String mur;

    @Column(name = "SMSA_UETR", columnDefinition = "VARCHAR2(255)")
    private String uetr;

    @Column(name = "SMSA_TXN_AMOUNT", columnDefinition = "NUMBER(*,2)")
    private BigDecimal transactionAmount;

    @Column(name = "SMSA_TXN_RESULT", columnDefinition = "VARCHAR2(255)")
    private String transactionResult;

    @Column(name = "SMSA_PRIMARY_FMT", columnDefinition = "VARCHAR2(255)")
    private String primaryFormat;

    @Column(name = "SMSA_SECONDARY_FMT", columnDefinition = "VARCHAR2(255)")
    private String secondaryFormat;

    @Column(name = "SMSA_MSG_CURRENCY", columnDefinition = "VARCHAR2(50)")
    private String currency;

    @Column(name = "SMSA_TRXN_RLTD_REFN", columnDefinition = "VARCHAR2(100)")
    private String transactionRelatedRefNo;

    @Column(name = "SMSA_VALUE_DATE", columnDefinition = "DATE")
    private LocalDate valueDate;

    @Column(name = "SMSA_EFFECTIVE_AMOUNT", columnDefinition = "NUMBER(*,2)")
    private BigDecimal effectiveAmount;

    @Column(name = "SMSA_EFFECTIVE_CCY", columnDefinition = "VARCHAR2(3)")
    private String effectiveCurrency;
    @Column(name = "SMSA_Message_2ndCopy", columnDefinition = "TIMESTAMP(6)")
    private String message2ndCopyDate;
    @Column(name = "SMSA_FIRCO_SOFT_STATUS", columnDefinition = "VARCHAR2(100)")
    private String fircoSoftStatus;

    public String getMessage2ndCopyDate() {
        return message2ndCopyDate;
    }

    public void setMessage2ndCopyDate(String message2ndCopyDate) {
        this.message2ndCopyDate = message2ndCopyDate;
    }

    public String getFircoSoftStatus() {
        return fircoSoftStatus;
    }

    public void setFircoSoftStatus(String fircoSoftStatus) {
        this.fircoSoftStatus = fircoSoftStatus;
    }

    public String getEffectiveCurrency() {
        return this.effectiveCurrency;
    }

    public void setEffectiveCurrency(String effectiveCurrency) {
        this.effectiveCurrency = effectiveCurrency;
    }

    public String getTransactionRelatedRefNo() {
        return this.transactionRelatedRefNo;
    }

    public void setTransactionRelatedRefNo(String transactionRelatedRefNo) {
        this.transactionRelatedRefNo = transactionRelatedRefNo;
    }

    public LocalDate getValueDate() {
        return this.valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public BigDecimal getEffectiveAmount() {
        return this.effectiveAmount;
    }

    public void setEffectiveAmount(BigDecimal effectiveAmount) {
        this.effectiveAmount = effectiveAmount;
    }

    /**
     * get field @Id
     * 
     * @Column(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @return id @Id
     * @Column(name = "SMSA_MESSAGE_ID")
     * 
     */
    public Long getId() {
        return this.messageId;
    }

    /**
     * set field @Id
     * 
     * @Column(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @param messageId @Id
     * @Column(name = "SMSA_MESSAGE_ID")
     * 
     */
    public void setId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * get field @Column(name = "SMSA_FILE_NAME")
     *
     * @return fileName @Column(name = "SMSA_FILE_NAME")
     * 
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * set field @Column(name = "SMSA_FILE_NAME")
     *
     * @param fileName @Column(name = "SMSA_FILE_NAME")
     * 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * get field @Column(name = "SMSA_DATE")
     *
     * @return date @Column(name = "SMSA_DATE")
     * 
     */
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * set field @Column(name = "SMSA_DATE")
     *
     * @param date @Column(name = "SMSA_DATE")
     * 
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * get field @Column(name = "SMSA_MT_CODE")
     *
     * @return mtCode @Column(name = "SMSA_MT_CODE")
     * 
     */
    public Integer getMtCode() {
        return this.mtCode;
    }

    /**
     * set field @Column(name = "SMSA_MT_CODE")
     *
     * @param mtCode @Column(name = "SMSA_MT_CODE")
     * 
     */
    public void setMtCode(Integer mtCode) {
        this.mtCode = mtCode;
    }

    /**
     * get field @Column(name = "SMSA_PAGE")
     *
     * @return page @Column(name = "SMSA_PAGE")
     * 
     */
    public Integer getPage() {
        return this.page;
    }

    /**
     * set field @Column(name = "SMSA_PAGE")
     *
     * @param page @Column(name = "SMSA_PAGE")
     * 
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * get field @Column(name = "SMSA_RAW_DATA")
     *
     * @return rawMessageData @Column(name = "SMSA_RAW_DATA")
     * 
     */
    public String getRawMessageData() {
        return this.rawMessageData;
    }

    /**
     * set field @Column(name = "SMSA_RAW_DATA")
     *
     * @param rawMessageData @Column(name = "SMSA_RAW_DATA")
     * 
     */
    public void setRawMessageData(String rawMessageData) {
        this.rawMessageData = rawMessageData;
    }

    /**
     * get field @Column(name = "SMSA_HDR_RAW")
     *
     * @return headerRaw @Column(name = "SMSA_HDR_RAW")
     * 
     */
    public String getHeaderRaw() {
        return this.headerRawJson;
    }

    /**
     * set field @Column(name = "SMSA_HDR_RAW")
     *
     * @param headerRawJson @Column(name = "SMSA_HDR_RAW")
     * 
     */
    public void setHeaderRaw(String headerRawJson) {
        this.headerRawJson = headerRawJson;
    }

    /**
     * get field @Column(name = "SMSA_HDR_TEXT")
     *
     * @return smsaHeaderObj @Column(name = "SMSA_HDR_TEXT")
     * 
     */
    public String getSmsaHeaderText() {
        return this.smsaHeaderText;
    }

    /**
     * set field @Column(name = "SMSA_HDR_TEXT")
     *
     * @param smsaHeaderText @Column(name = "SMSA_HDR_TEXT")
     * 
     */
    public void setSmsaHeaderText(String smsaHeaderText) {
        this.smsaHeaderText = smsaHeaderText;
    }

    /**
     * get field @Column(name = "SMSA_MSG_TYPE")
     *
     * @return msgType @Column(name = "SMSA_MSG_TYPE")
     * 
     */
    public String getMsgType() {
        return this.msgType;
    }

    /**
     * set field @Column(name = "SMSA_MSG_TYPE")
     *
     * @param msgType @Column(name = "SMSA_MSG_TYPE")
     * 
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * get field @Column(name = "SMSA_SENDER_BIC")
     *
     * @return senderBic @Column(name = "SMSA_SENDER_BIC")
     * 
     */
    public String getSenderBic() {
        return this.senderBic;
    }

    /**
     * set field @Column(name = "SMSA_SENDER_BIC")
     *
     * @param senderBic @Column(name = "SMSA_SENDER_BIC")
     * 
     */
    public void setSenderBic(String senderBic) {
        this.senderBic = senderBic;
    }

    // /**
    // * get field @Column(name = "SMSA_SENDER_OBJ")
    // *
    // * @return senderObj @Column(name = "SMSA_SENDER_OBJ")
    // *
    // */
    // public String getSenderObj() {
    // return this.senderObj;
    // }

    // /**
    // * set field @Column(name = "SMSA_SENDER_OBJ")
    // *
    // * @param senderObj @Column(name = "SMSA_SENDER_OBJ")
    // *
    // */
    // public void setSenderObj(String senderObj) {
    // this.senderObj = senderObj;
    // }

    /**
     * get field @Column(name = "SMSA_SENDER_BIC_DESC")
     *
     * @return senderBicDesc @Column(name = "SMSA_SENDER_BIC_DESC")
     * 
     */
    public String getSenderBicDesc() {
        return this.senderBicDesc;
    }

    /**
     * set field @Column(name = "SMSA_SENDER_BIC_DESC")
     *
     * @param senderBicDesc @Column(name = "SMSA_SENDER_BIC_DESC")
     * 
     */
    public void setSenderBicDesc(String senderBicDesc) {
        this.senderBicDesc = senderBicDesc;
    }

    // /**
    // * get field @Column(name = "SMSA_RECEIVER_OBJ")
    // *
    // * @return receiverObj @Column(name = "SMSA_RECEIVER_OBJ")
    // *
    // */
    // public String getReceiverObj() {
    // return this.receiverObj;
    // }

    // /**
    // * set field @Column(name = "SMSA_RECEIVER_OBJ")
    // *
    // * @param receiverObj @Column(name = "SMSA_RECEIVER_OBJ")
    // *
    // */
    // public void setReceiverObj(String receiverObj) {
    // this.receiverObj = receiverObj;
    // }

    /**
     * get field @Column(name = "SMSA_RECEIVER_BIC")
     *
     * @return receiverBic @Column(name = "SMSA_RECEIVER_BIC")
     * 
     */
    public String getReceiverBic() {
        return this.receiverBic;
    }

    /**
     * set field @Column(name = "SMSA_RECEIVER_BIC")
     *
     * @param receiverBic @Column(name = "SMSA_RECEIVER_BIC")
     * 
     */
    public void setReceiverBic(String receiverBic) {
        this.receiverBic = receiverBic;
    }

    /**
     * get field @Column(name = "SMSA_RECEIVER_BIC_DESC")
     *
     * @return receiverBicDesc @Column(name = "SMSA_RECEIVER_BIC_DESC")
     * 
     */
    public String getReceiverBicDesc() {
        return this.receiverBicDesc;
    }

    /**
     * set field @Column(name = "SMSA_RECEIVER_BIC_DESC")
     *
     * @param receiverBicDesc @Column(name = "SMSA_RECEIVER_BIC_DESC")
     * 
     */
    public void setReceiverBicDesc(String receiverBicDesc) {
        this.receiverBicDesc = receiverBicDesc;
    }

    /**
     * get field @Column(name = "SMSA_USER_REF")
     *
     * @return userRef @Column(name = "SMSA_USER_REF")
     * 
     */
    public String getMiorRef() {
        return this.miorRef;
    }

    public void setMiorRef(String miorRef) {
        this.miorRef = miorRef;
    }

    /**
     * get field @Column(name = "SMSA_UETR")
     *
     * @return uetr @Column(name = "SMSA_UETR")
     * 
     */
    public String getUetr() {
        return this.uetr;
    }

    /**
     * set field @Column(name = "SMSA_UETR")
     *
     * @param uetr @Column(name = "SMSA_UETR")
     * 
     */
    public void setUetr(String uetr) {
        this.uetr = uetr;
    }

    /**
     * get field @Column(name = "SMSA_TXN_REF")
     *
     * @return transactionRef @Column(name = "SMSA_TXN_REF")
     * 
     */
    public String getTransactionRef() {
        return this.transactionRef;
    }

    /**
     * set field @Column(name = "SMSA_TXN_REF")
     *
     * @param transactionRef @Column(name = "SMSA_TXN_REF")
     * 
     */
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    /**
     * get field @Column(name = "SMSA_SLA_ID")
     *
     * @return slaId @Column(name = "SMSA_SLA_ID")
     * 
     */
    public String getSlaId() {
        return this.slaId;
    }

    /**
     * set field @Column(name = "SMSA_SLA_ID")
     *
     * @param slaId @Column(name = "SMSA_SLA_ID")
     * 
     */
    public void setSlaId(String slaId) {
        this.slaId = slaId;
    }

    /**
     * get field @Column(name = "SMSA_MSG_IO")
     *
     * @return inpOut @Column(name = "SMSA_MSG_IO")
     * 
     */
    public String getInpOut() {
        return this.inpOut;
    }

    /**
     * set field @Column(name = "SMSA_MSG_IO")
     *
     * @param inpOut @Column(name = "SMSA_MSG_IO")
     * 
     */
    public void setInpOut(String inpOut) {
        this.inpOut = inpOut;
    }

    /**
     * get field @Column(name = "SMSA_MSG_DESC")
     *
     * @return msgDesc @Column(name = "SMSA_MSG_DESC")
     * 
     */
    public String getMsgDesc() {
        return this.msgDesc;
    }

    /**
     * set field @Column(name = "SMSA_MSG_DESC")
     *
     * @param msgDesc @Column(name = "SMSA_MSG_DESC")
     * 
     */
    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    /**
     * get field @Column(name = "SMSA_MUR")
     *
     * @return mur @Column(name = "SMSA_MUR")
     * 
     */
    public String getMur() {
        return this.mur;
    }

    /**
     * set field @Column(name = "SMSA_MUR")
     *
     * @param mur @Column(name = "SMSA_MUR")
     * 
     */
    public void setMur(String mur) {
        this.mur = mur;
    }

    /**
     * get field @Column(name = "SMSA_FILE_TYPE")
     *
     * @return fileType @Column(name = "SMSA_FILE_TYPE")
     * 
     */
    public String getFileType() {
        return this.fileType;
    }

    /**
     * set field @Column(name = "SMSA_FILE_TYPE")
     *
     * @param fileType @Column(name = "SMSA_FILE_TYPE")
     * 
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * get field @Column(name = "SMSA_TXN_AMOUNT")
     *
     * @return transactionAmount @Column(name = "SMSA_TXN_AMOUNT")
     * 
     */
    public BigDecimal getTransactionAmount() {
        return this.transactionAmount;
    }

    /**
     * set field @Column(name = "SMSA_TXN_AMOUNT")
     *
     * @param transactionAmount @Column(name = "SMSA_TXN_AMOUNT")
     * 
     */
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * get field @Column(name = "SMSA_TXN_RESULT")
     *
     * @return transactionResult @Column(name = "SMSA_TXN_RESULT")
     * 
     */
    public String getTransactionResult() {
        return this.transactionResult;
    }

    /**
     * set field @Column(name = "SMSA_TXN_RESULT")
     *
     * @param transactionResult @Column(name = "SMSA_TXN_RESULT")
     * 
     */
    public void setTransactionResult(String transactionResult) {
        this.transactionResult = transactionResult;
    }

    /**
     * get field @Column(name = "SMSA_PRIMARY_FMT")
     *
     * @return primaryFormat @Column(name = "SMSA_PRIMARY_FMT")
     * 
     */
    public String getPrimaryFormat() {
        return this.primaryFormat;
    }

    /**
     * set field @Column(name = "SMSA_PRIMARY_FMT")
     *
     * @param primaryFormat @Column(name = "SMSA_PRIMARY_FMT")
     * 
     */
    public void setPrimaryFormat(String primaryFormat) {
        this.primaryFormat = primaryFormat;
    }

    /**
     * get field @Column(name = "SMSA_SECONDARY_FMT")
     *
     * @return secondaryFormat @Column(name = "SMSA_SECONDARY_FMT")
     * 
     */
    public String getSecondaryFormat() {
        return this.secondaryFormat;
    }

    /**
     * set field @Column(name = "SMSA_SECONDARY_FMT")
     *
     * @param secondaryFormat @Column(name = "SMSA_SECONDARY_FMT")
     * 
     */
    public void setSecondaryFormat(String secondaryFormat) {
        this.secondaryFormat = secondaryFormat;
    }

    /**
     * get field @Column(name = "SMSA_MSG_CURRENCY")
     *
     * @return currency @Column(name = "SMSA_MSG_CURRENCY")
     * 
     */
    public String getCurrency() {
        return this.currency;
    }

    /**
     * set field @Column(name = "SMSA_MSG_CURRENCY")
     *
     * @param currency @Column(name = "SMSA_MSG_CURRENCY")
     * 
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * get field @Column(name = " SMSA_MSG_HDRID" , length=255)
     *
     * @return messageRefNo @Column(name = " SMSA_MSG_HDRID" , length=255)
     * 
     */
    public String getMessageHdrId() {
        return this.messageHdrId;
    }

    public void setMessageHdrId(String messageHdrId) {
        this.messageHdrId = messageHdrId;
    }

    /**
     * full file data as json
     */
    public String getMessageFullJson() {
        return this.messageFullJson;
    }

    /**
     * full file data as json
     */
    public void setMessageFullJson(String messageFullJson) {
        this.messageFullJson = messageFullJson;
    }

    /**
     * get field @Column(name = "SMSA_FILE_TIME", columnDefinition =
     * "Timestamp(10)")
     *
     * @return fileTime @Column(name = "SMSA_FILE_TIME", columnDefinition =
     *         "Timestamp(10)")
     * 
     */
    public String getFileTime() {
        return this.fileTime;
    }

    /**
     * set field @Column(name = "SMSA_FILE_TIME", columnDefinition =
     * "Timestamp(10)")
     *
     * @param fileTime @Column(name = "SMSA_FILE_TIME", columnDefinition =
     *                 "Timestamp(10)")
     * 
     */
    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    /**
     * get field @Column(name = "SMSA_FILE_DATE", columnDefinition = "DATE")
     *
     * @return fileDate @Column(name = "SMSA_FILE_DATE", columnDefinition = "DATE")
     * 
     */
    public LocalDate getFileDate() {
        return this.fileDate;
    }

    /**
     * set field @Column(name = "SMSA_FILE_DATE", columnDefinition = "DATE")
     *
     * @param fileDate @Column(name = "SMSA_FILE_DATE", columnDefinition = "DATE")
     * 
     */
    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    /**
     * get field @Column(name = "SMSA_GEO_ID", columnDefinition = "VARCHAR2(10)")
     *
     * @return geoId @Column(name = "SMSA_GEO_ID", columnDefinition =
     *         "VARCHAR2(10)")
     * 
     */
    public String getGeoId() {
        return this.geoId;
    }

    /**
     * set field @Column(name = "SMSA_GEO_ID", columnDefinition = "VARCHAR2(10)")
     *
     * @param geoId @Column(name = "SMSA_GEO_ID", columnDefinition = "VARCHAR2(10)")
     * 
     */
    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }
}
