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
public class SwiftMessageHeader implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_message_header_gen")
    @SequenceGenerator(name = "swift_message_header_gen", sequenceName = "swift_message_header", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;

    @Column(name = "SMSA_FILE_NAME")
    private String fileName;

    @Column(name = "SMSA_DATE")
    private LocalDateTime date;
    @Column(name = "SMSA_FILE_TIME")
    private String time;
    @Column(name = "SMSA_MT_CODE")
    private Integer mtCode;

    @Column(name = "SMSA_PAGE")
    private Integer page;

    @Column(name = "SMSA_RAW_DATA", columnDefinition = "CLOB")
    private String rawMessageData;// fill raw json data of 1 message

    @Lob
    @Column(name = "SMSA_HDR_OBJ", columnDefinition = "CLOB")
    private String headerRawJson;// full header raw data like prt file

    @Column(name = "SMSA_FILE_TYPE")
    private String fileType;

    @Column(name = "SMSA_PRIORITY")
    private String priority;

    @Column(name = "SMSA_HDR_TEXT", columnDefinition = "CLOB")
    private String smsaHeaderText;// header object

    @Column(name = "SMSA_INPUT_REF_NO")
    private String inputRefNo;

    @Column(name = "SMSA_OUTPUT_REF_NO")
    private String outputRefNo;

    @Column(name = "SMSA_MSG_IO")
    private String inpOut;

    @Column(name = "SMSA_MSG_DESC")
    private String msgDesc;

    @Column(name = "SMSA_MSG_TYPE")
    private String msgType;

    @Column(name = "SMSA_SLA_ID")
    private String slaId;

    @Column(name = "SMSA_SENDER_BIC")
    private String senderBic;

    @Lob
    @Column(name = "SMSA_SENDER_OBJ", columnDefinition = "CLOB")
    private String senderObj;

    @Column(name = "SMSA_SENDER_BIC_DESC")
    private String senderBicDesc;

    @Lob
    @Column(name = "SMSA_RECEIVER_OBJ", columnDefinition = "CLOB")
    private String receiverobj;

    @Column(name = "SMSA_RECEIVER_BIC")
    private String receiverBic;

    @Column(name = "SMSA_RECEIVER_BIC_DESC")
    private String receiverBicDesc;

    @Column(name = "SMSA_USER_REF")
    private String userRef;

    @Column(name = "SMSA_TXN_REF")
    private String transactionRef;

    @Column(name = "SMSA_FILE_DATE")
    private LocalDate fileDate;

    @Column(name = "SMSA_MUR")
    private String mur;

    @Column(name = "SMSA_UETR")
    private String uetr;

    @Column(name = "SMSA_TXN_AMOUNT")
    private BigDecimal transactionAmount;

    @Column(name = "SMSA_TXN_RESULT")
    private String transactionResult;

    @Column(name = "SMSA_PRIMARY_FMT")
    private String primaryFormat;

    @Column(name = "SMSA_SECONDARY_FMT")
    private String secondaryFormat;

    @Column(name = "SMSA_MSG_CURRENCY")
    private String currency;

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
     * get field @Column(name = "SMSA_FILE_TIME")
     *
     * @return time @Column(name = "SMSA_FILE_TIME")
     * 
     */
    public String getTime() {
        return this.time;
    }

    /**
     * set field @Column(name = "SMSA_FILE_TIME")
     *
     * @param time @Column(name = "SMSA_FILE_TIME")
     * 
     */
    public void setTime(String time) {
        this.time = time;
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
     * get field @Column(name = "SMSA_INPUT_REF_NO")
     *
     * @return inputRefNo @Column(name = "SMSA_INPUT_REF_NO")
     * 
     */
    public String getInputRefNo() {
        return this.inputRefNo;
    }

    /**
     * set field @Column(name = "SMSA_INPUT_REF_NO")
     *
     * @param inputRefNo @Column(name = "SMSA_INPUT_REF_NO")
     * 
     */
    public void setInputRefNo(String inputRefNo) {
        this.inputRefNo = inputRefNo;
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

    /**
     * get field @Column(name = "SMSA_SENDER_OBJ")
     *
     * @return senderObj @Column(name = "SMSA_SENDER_OBJ")
     * 
     */
    public String getSenderObj() {
        return this.senderObj;
    }

    /**
     * set field @Column(name = "SMSA_SENDER_OBJ")
     *
     * @param senderObj @Column(name = "SMSA_SENDER_OBJ")
     * 
     */
    public void setSenderObj(String senderObj) {
        this.senderObj = senderObj;
    }

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

    /**
     * get field @Column(name = "SMSA_RECEIVER_OBJ")
     *
     * @return receiverobj @Column(name = "SMSA_RECEIVER_OBJ")
     * 
     */
    public String getReceiverobj() {
        return this.receiverobj;
    }

    /**
     * set field @Column(name = "SMSA_RECEIVER_OBJ")
     *
     * @param receiverobj @Column(name = "SMSA_RECEIVER_OBJ")
     * 
     */
    public void setReceiverobj(String receiverobj) {
        this.receiverobj = receiverobj;
    }

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
    public String getUserRef() {
        return this.userRef;
    }

    /**
     * set field @Column(name = "SMSA_USER_REF")
     *
     * @param userRef @Column(name = "SMSA_USER_REF")
     * 
     */
    public void setUserRef(String userRef) {
        this.userRef = userRef;
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
     * get field @Column(name = "SMSA_FILE_DATE")
     *
     * @return fileDate @Column(name = "SMSA_FILE_DATE")
     * 
     */
    public LocalDate getFileDate() {
        return this.fileDate;
    }

    /**
     * set field @Column(name = "SMSA_FILE_DATE")
     *
     * @param fileDate @Column(name = "SMSA_FILE_DATE")
     * 
     */
    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
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
     * get field @Column(name = "SMSA_OUTPUT_REF_NO")
     *
     * @return outputRefNo @Column(name = "SMSA_OUTPUT_REF_NO")
     * 
     */
    public String getOutputRefNo() {
        return this.outputRefNo;
    }

    /**
     * set field @Column(name = "SMSA_OUTPUT_REF_NO")
     *
     * @param outputRefNo @Column(name = "SMSA_OUTPUT_REF_NO")
     * 
     */
    public void setOutputRefNo(String outputRefNo) {
        this.outputRefNo = outputRefNo;
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




}





