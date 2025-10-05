/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.entity;


/**
 *
 * @author abcom
 */


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
 * --------------------- Instance Type and Transmission --------------
 */
@Entity
@Table(name = "ISEC_INST_TXT")
public class SwiftMessageInstanceIsec  {
    @Id
    @Column(name = "SMSA_MESSAGE_ID", nullable = false)
    private Long messageId;

    @OneToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "SMSA_MESSAGE_ID")
    private SwiftMessageHeaderIsec header;

    /**
     * it stores Message Output Reference
     */
    @Column(name = "SMSA_MSG_OREF", columnDefinition = "VARCHAR2(100)")
    private String messageOutputReference;


    @Column(name = "SMSA_FILE_NAME" , columnDefinition = "VARCHAR2(100)")
    private String fileName;


    /**
     * it stores Correspondent input Reference
     */
    @Column(name = "SMSA_MSG_COREF", columnDefinition = "VARCHAR2(100)")
    private String correspondInRef;

    /**
     * 
     * it stores message input Reference from message text
     */
    @Column(name = "SMSA_MSG_IREF",  columnDefinition = "VARCHAR2(100)")
    private String MIR;

    /**
     * --- Instance Type and Transmission ----
     *
     * it stores first line of instance header as note
     */
    @Column(name = "SMSA_INST_NOTE", columnDefinition = "VARCHAR2(255)")
    private String note;
  /**
     * 
     * it stores Priority/Delivery  from message text
     */
    @Column(name = "SMSA_INST_PRTY", columnDefinition = "VARCHAR2(100)")
    private String priority;
    /**
     * --- Instance Type and Transmission ----
     *
     * It stores the NAK code in first line at start
     */
    @Column(name = "SMSA_NAK_CODE", columnDefinition = "VARCHAR2(50)")
    private String nakCode;

    /**
     * 
     * it stores full instance header raw as text all line until next header
     */
    @Lob
    @Column(name = "SMSA_INST_RAW")
    private String rawInstance;

    // /**
    // * it stores Correspondent input Reference
    // */
    // @Column(name = "SMSA_MSG_COIREF", length = 255)
    // private String CIR;

    /**
     * it stores full instance text as raw json
     */
    @Column(name = "SMSA_INST_JSON", columnDefinition = "JSON")
    private String instanceJson;










//Getters and  Setters

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
     * @return the messageOutputReference
     */
    public String getMessageOutputReference() {
        return messageOutputReference;
    }

    /**
     * @param messageOutputReference the messageOutputReference to set
     */
    public void setMessageOutputReference(String messageOutputReference) {
        this.messageOutputReference = messageOutputReference;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
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

    /**
     * get field @Column(name = "NAK_CODE")
     *
     * @return nakCode @Column(name = "NAK_CODE")
     * 
     */
    public String getNakCode() {
        return this.nakCode;
    }

    /**
     * set field @Column(name = "NAK_CODE")
     *
     * @param nakCode @Column(name = "NAK_CODE")
     * 
     */
    public void setNakCode(String nakCode) {
        this.nakCode = nakCode;
    }

    /**
     * get field @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
     *
     * @return rawInstance @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
     * 
     */
    public String getRawInstance() {
        return this.rawInstance;
    }

    /**
     * set field @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
     *
     * @param rawInstance @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
     * 
     */
    public void setRawInstance(String rawInstance) {
        this.rawInstance = rawInstance;
    }

    /**
     * get field @Column(name = "SMSA_MSG_COREF")
     *
     * @return correspondRef @Column(name = "SMSA_MSG_COREF")
     * 
     */
    public String getCorrespondInRef() {
        return this.correspondInRef;
    }

    /**
     * set field @Column(name = "SMSA_MSG_COREF")
     *
     * @param correspondInRef @Column(name = "SMSA_MSG_COREF")
     * 
     */
    public void setCorrespondInRef(String correspondInRef) {
        this.correspondInRef = correspondInRef;
    }

    /**
     * get field @Column(name = "SMSA_MSG_MIR", length = 255)
     *
     * @return MIR @Column(name = "SMSA_MSG_MIR", length = 255)
     * 
     */
    public String getMIR() {
        return this.MIR;
    }

    /**
     * set field @Column(name = "SMSA_MSG_MIR", length = 255)
     *
     * @param MIR @Column(name = "SMSA_MSG_MIR", length = 255)
     * 
     */
    public void setMIR(String MIR) {
        this.MIR = MIR;
    }

    // /**
    // * get field @Column(name = "SMSA_MSG_CIR", length = 255)
    // *
    // * @return CIR @Column(name = "SMSA_MSG_CIR", length = 255)
    // *
    // */
    // public String getCIR() {
    // return this.CIR;
    // }

    // /**
    // * set field @Column(name = "SMSA_MSG_CIR", length = 255)
    // *
    // * @param CIR @Column(name = "SMSA_MSG_CIR", length = 255)
    // *
    // */
    // public void setCIR(String CIR) {
    // this.CIR = CIR;
    // }

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
     * it stores full instance text as raw json
     */
    public String getInstanceJson() {
        return this.instanceJson;
    }

    /**
     * it stores full instance text as raw json
     */
    public void setInstanceJson(String instanceJson) {
        this.instanceJson = instanceJson;
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

