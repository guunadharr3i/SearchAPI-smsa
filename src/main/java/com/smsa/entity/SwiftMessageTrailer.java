package com.smsa.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SMSA_MSG_TRL")
public class SwiftMessageTrailer implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_msg_trl_gen")
    @SequenceGenerator(name = "swift_msg_trl_gen", sequenceName = "swift_message_trl", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;


    @Column(name = "SMSA_FILE_NAME")
    private String fileName;

    @Column(name = "SMSA_TXN_REF")
    private String txnRef;

    @Column(name = "SMSA_MSG_CHK")
    private String msgChk;

    @Column(name = "SMSA_MSG_SIG")
    private String msgSig;

    @Column(name = "SMSA_RMRK")
    private String remark;

    // Optional: link to parent if using bidirectional relationship
    // @ManyToOne
    // @JoinColumn(name = "SMSA_MESSAGE_ID", insertable = false, updatable = false)
    // private SmsaPrtMessageHdr messageHdr;

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
}
