package com.smsa.entity;

import java.io.Serializable;
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
 * -------------------------- Message Trailer -----------------------
 */
@Entity
@Table(name = "SMSA_MSG_TRL")
public class SwiftMessageTrailer implements Serializable{
    /**
     *
     * id primary key
     * 
     */
    @Id
    @Column(name = "SMSA_MESSAGE_ID", nullable = false)
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "SMSA_MESSAGE_ID")
    private SwiftMessageHeader header;

    /**
     *
     * file name of message
     * 
     */
    @Column(name = "SMSA_FILE_NAME", columnDefinition = "VARCHAR2(100)")
    private String fileName;

    /**
     *
     * Transaction Reference Number from message text
     * 
     */
    @Column(name = "SMSA_TXN_REF", columnDefinition = "VARCHAR2(100)")
    private String txnRef;

    /**
     *
     * CHK from message trailer
     * 
     */
    @Column(name = "SMSA_MSG_CHK", columnDefinition = "VARCHAR2(100)")
    private String msgChk;

    /**
     *
     * SIG from message trailer
     * 
     */
    @Column(name = "SMSA_MSG_SIG", columnDefinition = "VARCHAR2(100)")
    private String msgSig;
    /**
     *
     * TNG from message trailer
     * 
     */
    @Column(name = "SMSA_MSG_TNG", columnDefinition = "VARCHAR2(100)")
    private String msgTNG;

    /**
     *
     * remark from message instance
     * 
     */
    @Column(name = "SMSA_RMRK", columnDefinition = "VARCHAR2(255)")
    private String remark;

    /**
     *
     * storing raw message trailer as text
     * 
     */
    @Lob
    @Column(name = "SMSA_TRL_RAW")
    private String trailerRaw;

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
     * @return the txnRef
     */
    public String getTxnRef() {
        return txnRef;
    }

    /**
     * @param txnRef the txnRef to set
     */
    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    /**
     * @return the msgChk
     */
    public String getMsgChk() {
        return msgChk;
    }

    /**
     * @param msgChk the msgChk to set
     */
    public void setMsgChk(String msgChk) {
        this.msgChk = msgChk;
    }

    /**
     * @return the msgSig
     */
    public String getMsgSig() {
        return msgSig;
    }

    /**
     * @param msgSig the msgSig to set
     */
    public void setMsgSig(String msgSig) {
        this.msgSig = msgSig;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * get field @Column(name = "SMSA_TRL_RAW")
     *
     * @return trailerRaw @Column(name = "SMSA_TRL_RAW")
     * 
     */
    public String getTrailerRaw() {
        return this.trailerRaw;
    }

    /**
     * set field @Column(name = "SMSA_TRL_RAW")
     *
     * @param trailerRaw @Column(name = "SMSA_TRL_RAW")
     * 
     */
    public void setTrailerRaw(String trailerRaw) {
        this.trailerRaw = trailerRaw;
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
     *
     * TNG from message trailer
     *
     */
    public String getMsgTNG() {
        return this.msgTNG;
    }

    /**
     *
     * TNG from message trailer
     *
     */
    public void setMsgTNG(String msgTNG) {
        this.msgTNG = msgTNG;
    }
}
