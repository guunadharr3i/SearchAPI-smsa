package com.smsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "SMSA_INST_TXT")
public class SwiftMessageInstance implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_msg_inst_gen")
    @SequenceGenerator(name = "swift_msg_inst_gen", sequenceName = "swift_message_inst", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;


    @Column(name = "SMSA_MSG_OREF")
    @JsonProperty("Message Output Reference")
    private String messageOutputReference;


    @Column(name = "SMSA_MSG_COREF")
  private String correspondRef;

    @Column(name = "SMSA_INST_NOTE")
    private String note;

    @Column(name = "SMSA_INST_PRIY")
    private String priority;



    @Column(name = "NAK_CODE")
    private String nakCode;

    @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
    private String rawInstance;

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

     */
    public String getNakCode() {
        return this.nakCode;
    }

    /**
     * set field @Column(name = "NAK_CODE")
     *
     * @param nakCode @Column(name = "NAK_CODE")

     */
    public void setNakCode(String nakCode) {
        this.nakCode = nakCode;
    }

    /**
     * get field @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
     *
     * @return rawInstance @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")

     */
    public String getRawInstance() {
        return this.rawInstance;
    }

    /**
     * set field @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")
     *
     * @param rawInstance @Column(name = "SMSA_INST_RAW",columnDefinition = "CLOB")

     */
    public void setRawInstance(String rawInstance) {
        this.rawInstance = rawInstance;
    }

    /**
     * get field @Column(name = "SMSA_MSG_COREF")
     *
     * @return correspondRef @Column(name = "SMSA_MSG_COREF")

     */
    public String getCorrespondRef() {
        return this.correspondRef;
    }

    /**
     * set field @Column(name = "SMSA_MSG_COREF")
     *
     * @param correspondRef @Column(name = "SMSA_MSG_COREF")

     */
    public void setCorrespondRef(String correspondRef) {
        this.correspondRef = correspondRef;
    }

}


