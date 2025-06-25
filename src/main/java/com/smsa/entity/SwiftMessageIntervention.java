package com.smsa.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SMSA_MSG_INTVN")
public class SwiftMessageIntervention implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_msg_int_gen")
    @SequenceGenerator(name = "swift_msg_txt_gen", sequenceName = "swift_message_int", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;


 @Column(name = "SMSA_FILE_NAME")
    private String  fileName;

 @Column(name = "SMSA_TXN_REF")
    private String  transactionRef;


 @Column(name = "SMSA_RMRK")
    private String  remark;


// @Column(name = "SMSA_INTVN")
//    private String  remark;

    /**
     * @return the id
     */
    public Long getId() {
        return messageId;
    }

    /**
     * @param messageId the id to set
     */
    public void setId(Long messageId) {
        this.messageId = messageId;
    }


    /**
     * get field @Column(name = "SMSA_RMRK")
     *
     * @return remark @Column(name = "SMSA_RMRK")

     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * set field @Column(name = "SMSA_RMRK")
     *
     * @param remark @Column(name = "SMSA_RMRK")

     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * get field @Column(name = "SMSA_TXN_REF")
     *
     * @return transactionRef @Column(name = "SMSA_TXN_REF")

     */
    public String getTransactionRef() {
        return this.transactionRef;
    }

    /**
     * set field @Column(name = "SMSA_TXN_REF")
     *
     * @param transactionRef @Column(name = "SMSA_TXN_REF")

     */
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    /**
     * get field @Column(name = "SMSA_FILE_NAME")
     *
     * @return fileName @Column(name = "SMSA_FILE_NAME")

     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * set field @Column(name = "SMSA_FILE_NAME")
     *
     * @param fileName @Column(name = "SMSA_FILE_NAME")

     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
