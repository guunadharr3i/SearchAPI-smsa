package com.smsa.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SMSA_MSG_INTVN")
public class SwiftMessageIntervention implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_msg_int_gen")
    @SequenceGenerator(name = "swift_msg_txt_gen", sequenceName = "swift_message_int", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;

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

    
}