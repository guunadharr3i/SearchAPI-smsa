package com.smsa.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SMSA_USER_LOGON_HST_DTL")
public class UserLogonHstDtl {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Or SEQUENCE if using Oracle
    @Column(name = "Logon_ID")
    private Long Id;


    @Column(name = "Logon_date")
    private LocalDateTime logonDate;

    @Column(name = "Device_hash", length = 150)
    private String deviceHash;

    @Column(name = "Access_token", length = 5000)
    private String accessToken;

    @Column(name = "User_id", length = 12)
    private String userId;

    @Column(name = "logoff_date")
    private LocalDateTime logoffDate;

    @Column(name = "Remarks", length = 150)
    private String remarks;

    // Getters and Setters

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalDateTime getLogonDate() {
        return logonDate;
    }

    public void setLogonDate(LocalDateTime logonDate) {
        this.logonDate = logonDate;
    }

    public String getDeviceHash() {
        return deviceHash;
    }

    public void setDeviceHash(String deviceHash) {
        this.deviceHash = deviceHash;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getLogoffDate() {
        return logoffDate;
    }

    public void setLogoffDate(LocalDateTime logoffDate) {
        this.logoffDate = logoffDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
