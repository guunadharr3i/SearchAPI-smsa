package com.smsa.entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SMSA_MSG_TXT")
public class SwiftMessageText  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_msg_txt_gen")
    @SequenceGenerator(name = "swift_msg_txt_gen", sequenceName = "swift_message_txt", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long messageId;



    @Column(name = "SMSA_MSG_STRING", columnDefinition = "CLOB")
     private String raw_messageText;

      @Column(name = "SMSA_TXN_REF")
     private String transactionRef;

      @Column(name = "SMSA_FILE_NAME")
     private String fileName;

    @Lob
      @Column(name = "SMSA_MSG_TXTJ", columnDefinition = "CLOB")
     private String messageRaw;

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
     * get field @Lob
     @Column(name = "SMSA_MSG_TXTOBJ", columnDefinition = "CLOB")

      *
      * @return raw_messageText @Lob
     @Column(name = "SMSA_MSG_TXTOBJ", columnDefinition = "CLOB")

     */
    public String getRaw_messageText() {
        return this.raw_messageText;
    }

    /**
     * set field @Lob
     @Column(name = "SMSA_MSG_TXTOBJ", columnDefinition = "CLOB")

      *
      * @param raw_messageText @Lob
     @Column(name = "SMSA_MSG_TXTOBJ", columnDefinition = "CLOB")

     */
    public void setRaw_messageText(String raw_messageText) {
        this.raw_messageText = raw_messageText;
    }

    /**
     * get field @Column(name = "SMSA_MSG_STRING")
     *
     * @return messageRaw @Column(name = "SMSA_MSG_STRING")

     */
    public String getMessageRaw() {
        return this.messageRaw;
    }

    /**
     * set field @Column(name = "SMSA_MSG_STRING")
     *
     * @param messageRaw @Column(name = "SMSA_MSG_STRING")

     */
    public void setMessageRaw(String messageRaw) {
        this.messageRaw = messageRaw;
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
