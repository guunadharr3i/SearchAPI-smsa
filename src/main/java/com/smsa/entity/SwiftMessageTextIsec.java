/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.entity;



import java.math.BigDecimal;
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

/**
 * --------------------------- Message Text ---------------------------
 */
@Entity
@Table(name = "ISEC_MSG_TXT")
public class SwiftMessageTextIsec {
    @Id
    @Column(name = "SMSA_MESSAGE_ID", nullable = false)
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "SMSA_MESSAGE_ID")
    private SwiftMessageHeaderIsec header;

    /**
     * it stores full Message text as raw String
     */
    @Lob
    @Column(name = "SMSA_MSG_RAW")
    private String raw_messageText;
    /**
     * it stores Transaction Reference from field 20
     */
    // @Column(name = "SMSA_TXN_REF", columnDefinition = "VARCHAR2(100)")
    // private String transactionRef;

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
    @Column(name = "SMSA_TRXN_ACC_ID", columnDefinition = "VARCHAR2(100)")
    private String accountId;

    @Column(name = "SMSA_MSG_OPENING_60F", columnDefinition = "NUMBER(*,2)")
    private BigDecimal openingBal60F;

    @Column(name = "SMSA_CLOSING_62F", columnDefinition = "NUMBER(*,2)")
    private BigDecimal closingBal62F;

    @Column(name = "SMSA_AVAILABLE_64", columnDefinition = "NUMBER(*,2)")
    private BigDecimal availableBalance64;

    @Column(name = "SMSA_MSG_CLOSE_CCY", columnDefinition = "VARCHAR2(10)")
    private String closingBal62FCurrency;

    @Column(name = "SMSA_MSG_OPEN_CCY", columnDefinition = "VARCHAR2(10)")
    private String openingBal60FCurrency;

    @Column(name = "SMSA_MSG_AVAIL_CCY", columnDefinition = "VARCHAR2(10)")
    private String availBal64Currency;

    // @Column(name = "SMSA_MSG_VALDATE(60)", columnDefinition = "VARCHAR2(10)")
    // private String valueDate60;
    @Column(name = "SMSA_MSG_VALDATE62F", columnDefinition = "VARCHAR2(50)")
    private String valueDate62F;
    @Column(name = "SMSA_MSG_VALDATE64", columnDefinition = "VARCHAR2(50)")
    private String valueDate64;

    @Column(name = "SMSA_MSG_VALDATE60F", columnDefinition = "VARCHAR2(50)")
    private String valueDate60F;

    @Column(name = "SMSA_MSG_STMT_NUMBER", columnDefinition = "NUMBER(5,0)")
    private Integer statementNumber;

    @Column(name = "SMSA_MSG_SEQ_NUMBER", columnDefinition = "NUMBER(5,0)")
    private Integer sequenceNumber;

    @Column(name = "SMSA_MSG_CLOSE_CRED", columnDefinition = "VARCHAR2(6)")
    private String isCloseCreditDebit;

    @Column(name = "SMSA_MSG_OPEN_CRED", columnDefinition = "VARCHAR2(6)")
    private String isOpenCreditDebit;

    @Column(name = "SMSA_MSG_AVAIL_CRED", columnDefinition = "VARCHAR2(6)")
    private String isAvailableCreDebit;
    /**
     * it stores transaction related reference number from field 251
     * //
     */
    @Column(name = "SMSA_MSG_STMNT", columnDefinition = "JSON")
    private String statementLine;

    // @Column(name = "SMSA_MSG_INFO_ACC", columnDefinition = "VARCHAR2(250)")
    // private String infoToAccountOwner;
    // @Column(name = "SMSA_TRXN_RLTD_REFN" , columnDefinition = "VARCHAR2(100)")
    // private String transactionRelatedRefNo;

    public String getValueDate62F() {
        return this.valueDate62F;
    }

    public void setValueDate62F(String valueDate62F) {
        this.valueDate62F = valueDate62F;
    }

    public String getValueDate64() {
        return this.valueDate64;
    }

    public void setValueDate64(String valueDate64) {
        this.valueDate64 = valueDate64;
    }
    public BigDecimal getOpeningBal60F() {
        return this.openingBal60F;
    }

    public void setOpeningBal60F(BigDecimal openingBal60F) {
        this.openingBal60F = openingBal60F;
    }

    public BigDecimal getClosingBal62F() {
        return this.closingBal62F;
    }

    public void setClosingBal62F(BigDecimal closingBal62F) {
        this.closingBal62F = closingBal62F;
    }

    public BigDecimal getAvailableBalance64() {
        return this.availableBalance64;
    }

    public void setAvailableBalance64(BigDecimal availableBalance64) {
        this.availableBalance64 = availableBalance64;
    }

    public String getClosingBal62FCurrency() {
        return this.closingBal62FCurrency;
    }

    public void setClosingBal62FCurrency(String closingBal62FCurrency) {
        this.closingBal62FCurrency = closingBal62FCurrency;
    }

    public String getOpeningBal60FCurrency() {
        return this.openingBal60FCurrency;
    }

    public void setOpeningBal60FCurrency(String openingBal60FCurrency) {
        this.openingBal60FCurrency = openingBal60FCurrency;
    }

    public String getAvailBal64Currency() {
        return this.availBal64Currency;
    }

    public void setAvailBal64Currency(String availBal64Currency) {
        this.availBal64Currency = availBal64Currency;
    }

    public String getValueDate60F() {
        return this.valueDate60F;
    }

    public void setValueDate60F(String valueDate60F) {
        this.valueDate60F = valueDate60F;
    }

    public Integer getStatementNumber() {
        return this.statementNumber;
    }

    public void setStatementNumber(Integer statementNumber) {
        this.statementNumber = statementNumber;
    }

    public Integer getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getIsCloseCreditDebit() {
        return this.isCloseCreditDebit;
    }

    public void setIsCloseCreditDebit(String isCloseCreditDebit) {
        this.isCloseCreditDebit = isCloseCreditDebit;
    }

    public String getIsOpenCreditDebit() {
        return this.isOpenCreditDebit;
    }

    public void setIsOpenCreditDebit(String isOpenCreditDebit) {
        this.isOpenCreditDebit = isOpenCreditDebit;
    }

    public String getIsAvailableCreDebit() {
        return this.isAvailableCreDebit;
    }

    public void setIsAvailableCreDebit(String isAvailableCreDebit) {
        this.isAvailableCreDebit = isAvailableCreDebit;
    }

    public String getStatementLine() {
        return this.statementLine;
    }

    public void setStatementLine(String statementLine) {
        this.statementLine = statementLine;
    }

    // public String getInfoToAccountOwner() {
    //     return this.infoToAccountOwner;
    // }

    // public void setInfoToAccountOwner(String infoToAccountOwner) {
    //     this.infoToAccountOwner = infoToAccountOwner;
    // }

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

    // /**
    // * get field @Column(name = "SMSA_TXN_REF")
    // *
    // * @return transactionRef @Column(name = "SMSA_TXN_REF")
    // *
    // */
    // public String getTransactionRef() {
    // return this.transactionRef;
    // }

    // /**
    // * set field @Column(name = "SMSA_TXN_REF")
    // *
    // * @param transactionRef @Column(name = "SMSA_TXN_REF")
    // *
    // */
    // public void setTransactionRef(String transactionRef) {
    // this.transactionRef = transactionRef;
    // }

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
    public SwiftMessageHeaderIsec getHeader() {
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
    public void setHeader(SwiftMessageHeaderIsec header) {
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

}