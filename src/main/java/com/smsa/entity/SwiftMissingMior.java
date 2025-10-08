package com.smsa.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SMSA_MIOR_TCK")
public class SwiftMissingMior   {

    @Id
    @Column(name = "SMSA_MESSAGE_ID", nullable = false)
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "SMSA_MESSAGE_ID")
    private SwiftMessageHeader header;

    @Column(name = "SMSA_MIOR_SESSION_NO", columnDefinition = "NUMBER(6,0)", nullable = false)
    private Integer sessionNo;

    @Column(name = "SMSA_MIOR_SEQUENCE_NO", columnDefinition = "NUMBER(9,0)", nullable = false)
    private Integer sequenceNo;

    @Column(name = "SMSA_MIOR_BIC", columnDefinition = "VARCHAR2(255)")
    private String miorBic;

    @Column(name = "SMSA_FILE_DATE", columnDefinition = "DATE", nullable = false)
    private LocalDate fileDate;

    @Column(name = "SMSA_MIOR_REF", columnDefinition = "VARCHAR2(100)")
    private String miorRawValue;

    @Column(name = "SMSA_MIOR_FLAG", columnDefinition = "NUMBER(1,0)", nullable = false)
    private Integer miorFlag;

    @Column(name = "SMSA_MIOR_GEO", columnDefinition = "VARCHAR(3)", nullable = false)
    private String miorGeo;

    @Column(name = "SMSA_MSG_IO", columnDefinition = "VARCHAR(1)", nullable =
    false)
    private String msgIo;

    /**
     * get field @Id
     * 
     * @Column(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @return messageId @Id
     * @Column(name = "SMSA_MESSAGE_ID")
     * 
     */
    public Long getMessageId() {
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
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * get field @MapsId
     * 
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @return header @MapsId
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     * 
     */
    public SwiftMessageHeader getHeader() {
        return this.header;
    }

    /**
     * set field @MapsId
     * 
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     *
     * 
     * @param header @MapsId
     * @JoinColumn(name = "SMSA_MESSAGE_ID")
     * 
     */
    public void setHeader(SwiftMessageHeader header) {
        this.header = header;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_SESSION_NO")
     *
     * @return sessionNo @Column(name = "SMSA_MIOR_SESSION_NO")
     * 
     */
    public Integer getSessionNo() {
        return this.sessionNo;
    }

    /**
     * set field @Column(name = "SMSA_MIOR_SESSION_NO")
     *
     * @param sessionNo @Column(name = "SMSA_MIOR_SESSION_NO")
     * 
     */
    public void setSessionNo(Integer sessionNo) {
        this.sessionNo = sessionNo;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_SEQUENCE_NO")
     *
     * @return sequenceNo @Column(name = "SMSA_MIOR_SEQUENCE_NO")
     * 
     */
    public Integer getSequenceNo() {
        return this.sequenceNo;
    }

    /**
     * set field @Column(name = "SMSA_MIOR_SEQUENCE_NO")
     *
     * @param sequenceNo @Column(name = "SMSA_MIOR_SEQUENCE_NO")
     * 
     */
    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_BIC", length = 15)
     *
     * @return miorBic @Column(name = "SMSA_MIOR_BIC", length = 15)
     * 
     */
    public String getMiorBic() {
        return this.miorBic;
    }

    /**
     * set field @Column(name = "SMSA_MIOR_BIC", length = 15)
     *
     * @param miorBic @Column(name = "SMSA_MIOR_BIC", length = 15)
     * 
     */
    public void setMiorBic(String miorBic) {
        this.miorBic = miorBic;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_DATE")
     *
     * @return miorDate @Column(name = "SMSA_MIOR_DATE")
     * 
     */
    public LocalDate getMiorDate() {
        return this.fileDate;
    }

   
    public void setMiorDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_RAW_VALUE", length = 50)
     *
     * @return miorRawValue @Column(name = "SMSA_MIOR_RAW_VALUE", length = 50)
     * 
     */
    public String getMiorRawValue() {
        return this.miorRawValue;
    }

    /**
     * set field @Column(name = "SMSA_MIOR_RAW_VALUE", length = 50)
     *
     * @param miorRawValue @Column(name = "SMSA_MIOR_RAW_VALUE", length = 50)
     * 
     */
    public void setMiorRawValue(String miorRawValue) {
        this.miorRawValue = miorRawValue;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_FLAG", columnDefinition = "NUMBER(1,0)",
     * nullable = false)
     *
     * @return miorFlag @Column(name = "SMSA_MIOR_FLAG", columnDefinition =
     *         "NUMBER(1,0)", nullable = false)
     * 
     */
    public Integer getMiorFlag() {
        return this.miorFlag;
    }

    /**
     * set field @Column(name = "SMSA_MIOR_FLAG", columnDefinition = "NUMBER(1,0)",
     * nullable = false)
     *
     * @param miorFlag @Column(name = "SMSA_MIOR_FLAG", columnDefinition =
     *                 "NUMBER(1,0)", nullable = false)
     * 
     */
    public void setMiorFlag(Integer miorFlag) {
        this.miorFlag = miorFlag;
    }

    /**
     * get field @Column(name = "SMSA_MIOR_GEO", columnDefinition = "VARCHAR(3)",
     * nullable = false)
     *
     * @return miorGeo @Column(name = "SMSA_MIOR_GEO", columnDefinition =
     *         "VARCHAR(3)", nullable = false)
     * 
     */
    public String getMiorGeo() {
        return this.miorGeo;
    }

    /**
     * set field @Column(name = "SMSA_MIOR_GEO", columnDefinition = "VARCHAR(3)",
     * nullable = false)
     *
     * @param miorGeo @Column(name = "SMSA_MIOR_GEO", columnDefinition =
     *                "VARCHAR(3)", nullable = false)
     * 
     */
    public void setMiorGeo(String miorGeo) {
        this.miorGeo = miorGeo;
    }

    /**
     * get field @Column(name = "SMSA_MSG_IO", columnDefinition = "VARCHAR(1)", nullable =
     false)
     *
     * @return msgIo @Column(name = "SMSA_MSG_IO", columnDefinition = "VARCHAR(1)", nullable =
    false)

     */
    public String getMsgIo() {
        return this.msgIo;
    }

    /**
     * set field @Column(name = "SMSA_MSG_IO", columnDefinition = "VARCHAR(1)", nullable =
     false)
     *
     * @param msgIo @Column(name = "SMSA_MSG_IO", columnDefinition = "VARCHAR(1)", nullable =
    false)

     */
    public void setMsgIo(String msgIo) {
        this.msgIo = msgIo;
    }

 
}