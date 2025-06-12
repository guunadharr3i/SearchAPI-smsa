package com.smsa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "SMSA_INST_TXT")
public class SwiftMessageInstance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_msg_inst_gen")
    @SequenceGenerator(name = "swift_msg_inst_gen", sequenceName = "swift_message_inst", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;


    @Column(name = "SMSA_MSG_OREF")
    @JsonProperty("Message Output Reference")
    private String messageOutputReference;

    @Column(name = "SMSA_INST_NOTE")
    private String note;

    @Column(name = "priority")
    private String priority;

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


}
