package com.smsa.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "SMSA_MSG_TXT")
public class SwiftMessageText implements Serializable{
    @Id
    @Column(name = "SMSA_MESSAGE_ID", nullable = false)
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "SMSA_MESSAGE_ID")
    private SwiftMessageHeader header;

    /**
     * it stores full Message text as raw String
     */
    @Lob
    @Column(name = "SMSA_MSG_RAW")
    private String raw_messageText;
    /**
     * it stores Transaction Reference from field 20
     */
    @Column(name = "SMSA_TXN_REF", columnDefinition = "VARCHAR2(100)")
    private String transactionRef;

    @Column(name = "SMSA_FILE_NAME", columnDefinition = "VARCHAR2(100)")
    private String fileName;

    /**
     * it stores full Message text as JSON
     */
    @Column(name = "SMSA_MSG_TXTJ", columnDefinition = "JSON")
    private String messageJson;

    /**
     * it stores Account Identification from field 25
     */
   @Column(name = "SMSA_TRXN_ACC_ID" , columnDefinition = "VARCHAR2(100)")
    private String accountId;


      /**
     * it stores transaction related reference number from field 251
     */ 
    @Column(name = "SMSA_TRXN_RLTD_REFN" , columnDefinition = "VARCHAR2(100)")
    private String transactionRelatedRefNo;



   
    public Long getMessageId() {
        return this.messageId;
    }


     
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * get field @Column(name = "SMSA_MSG_STRING", columnDefinition = "CLOB")
     *
     * @return raw_messageText @Column(name = "SMSA_MSG_STRING", columnDefinition =
     *         "CLOB")
     * 
     */
    public String getRaw_messageText() {
        return this.raw_messageText;
    }

    /**
     * set field @Column(name = "SMSA_MSG_STRING", columnDefinition = "CLOB")
     *
     * @param raw_messageText @Column(name = "SMSA_MSG_STRING", columnDefinition =
     *                        "CLOB")
     * 
     */
    public void setRaw_messageText(String raw_messageText) {
        this.raw_messageText = raw_messageText;
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
     * get field @Lob
     * 
     * @Column(name = "SMSA_MSG_TXTJ", columnDefinition = "CLOB")
     *
     * 
     * @return messageRaw @Lob
     * @Column(name = "SMSA_MSG_TXTJ", columnDefinition = "CLOB")
     * 
     */
    public String getMessageJson() {
        return this.messageJson;
    }

    /**
     * set field @Lob
     * 
     * @Column(name = "SMSA_MSG_TXTJ", columnDefinition = "CLOB")
     *
     * 
     * @param messageRaw @Lob
     * @Column(name = "SMSA_MSG_TXTJ", columnDefinition = "CLOB")
     * 
     */
    public void setMessageJson(String messageRaw) {
        this.messageJson = messageRaw;
    }

    /**
     * get field @OneToOne
     * 
     * @MapsId
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @return header @OneToOne
     * @MapsId
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     * 
     */
    public SwiftMessageHeader getHeader() {
        return this.header;
    }

    /**
     * set field @OneToOne
     * 
     * @MapsId
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @param header @OneToOne
     * @MapsId
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     * 
     */
    public void setHeader(SwiftMessageHeader header) {
        this.header = header;
    }

    /**
     * it stores Account Identification from field 25
     */
    public String getAccountId() {
        return this.accountId;
    }

    /**
     * it stores Account Identification from field 25
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * it stores transaction related reference number from field 251
     */
    public String getTransactionRelatedRefNo() {
        return this.transactionRelatedRefNo;
    }

    /**
     * it stores transaction related reference number from field 251
     */
    public void setTransactionRelatedRefNo(String transactionRelatedRefNo) {
        this.transactionRelatedRefNo = transactionRelatedRefNo;
    }
}