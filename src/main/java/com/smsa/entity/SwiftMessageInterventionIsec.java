/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 * ---------------------------- Interventions -------------------------
 */
@Entity
@Table(name = "ISEC_MSG_INTVN")
public class SwiftMessageInterventionIsec {
    @Id
    @Column(name = "SMSA_MESSAGE_ID", nullable = false)
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "SMSA_MESSAGE_ID")
    private SwiftMessageHeaderIsec header;

    @Column(name = "SMSA_FILE_NAME", columnDefinition = "VARCHAR2(100)")
    private String fileName;

    @Column(name = "SMSA_TXN_REF", columnDefinition = "VARCHAR2(100)")
    private String transactionRef;

    @Column(name = "SMSA_RMRK", columnDefinition = "VARCHAR2(255)")
    private String remark;

    /**
     *
     * inside category taking intervention
     * from list takin 0 index
     */
    @Column(name = "SMSA_INTVN_CAT", columnDefinition = "VARCHAR2(255)")
    private String category;

    /**
     *
     * inside category taking creationTime
     * from list takin 0 index
     */
    @Column(name = "SMSA_INTVN_CRTIME", columnDefinition = "VARCHAR2(255)")
    private String creationTime;

    /**
     *
     * inside category taking operator
     * from list takin 0 index
     */
    @Column(name = "SMSA_INTVN_OPRTR", columnDefinition = "VARCHAR2(255)")
    private String operator;
    // /**
    // *
    // * storing full intervention as raw text
    // *
    // */
    @Lob
    @Column(name = "SMSA_INTVN_RAW")
    private String interventionRawString;


    @Column(name = "SMSA_INTVN_RAW_JSON", columnDefinition = "JSON")
    private String interventionRawJson;

    /**
     * storing full intervention as raw text
     *
     */;

    public String getInterventionRawJson() {
        return interventionRawJson;
    }

    public void setInterventionRawJson(String interventionRawJson) {
        this.interventionRawJson = interventionRawJson;
    }

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
     * 
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * set field @Column(name = "SMSA_RMRK")
     *
     * @param remark @Column(name = "SMSA_RMRK")
     * 
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     *
     * inside category taking intervention
     * from list takin 0 index
     */
    public String getCategory() {
        return this.category;
    }

    /**
     *
     * inside category taking intervention
     * from list takin 0 index
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * inside category taking creationTime
     * from list takin 0 index
     */
    public String getCreationTime() {
        return this.creationTime;
    }

    /**
     *
     * inside category taking creationTime
     * from list takin 0 index
     */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    /**
     *
     * inside category taking operator
     * from list takin 0 index
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     *
     * inside category taking operator
     * from list takin 0 index
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setInterventionRaw(String interventionRawString) {
        this.interventionRawString = interventionRawString;
    }

    public String getInterventionRaw() {
        return this.interventionRawString;
    }

   
}
