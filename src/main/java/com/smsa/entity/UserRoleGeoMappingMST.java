/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.entity;

/**
 *
 * @author abcom
 */
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "SMSA_USER_ROLE_GEO_MAPPING_MST")
public class UserRoleGeoMappingMST {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Or SEQUENCE if using Oracle
    @Column(name = "id")
    private Long id;

    @Column(name = "User_id", length = 12, nullable = false)
    private String userId;


    @Column(name = "Role_id", length = 8, nullable = false)
    private Long roleId;

    @Column(name = "LAM_number", length = 30)
    private String lamNumber;

    @Column(name = "Role_Name", length = 30)
    private String roleName;

    @Column(name = "Geo_id", length = 8)
    private Long geoId;

    @Column(name = "MODULE_ID", length = 8)
    private Long moduleId;

    @Column(name = "GEO_Name", length = 150)
    private String geoName;

    @Column(name = "Created_date")
    private LocalDateTime createdDate;

    @Column(name = "Updated_at_date")
    private LocalDateTime updatedDate;

    @Column(name = "Deleted_at_date")
    private LocalDateTime deletedDate;

    @Column(name = "Created_BY")
    private String createdBy;

    @Column(name = "Active_status", length = 1)
    private String activeStatus;

    @Column(name = "Remarks", length = 150)
    private String remarks;

    @Column(name = "User_Name",length = 30)
    private String userName;

    @Column(name = "User_Location")
    private String userLocation;

    @Column(name = "Sol_Id")
    private String solId;

    @Column(name = "Email_Id")
    private String emailId;

    @Column(name = "is_locked")
    private String isLock;

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getLamNumber() {
        return lamNumber;
    }

    public void setLamNumber(String lamNumber) {
        this.lamNumber = lamNumber;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getGeoId() {
        return geoId;
    }

    public void setGeoId(Long geoId) {
        this.geoId = geoId;
    }

    public Long getModuleId(){
        return moduleId;
    }

    public void setModuleId(Long moduleId){
        this.moduleId = moduleId;
    }

    public String getGeoName() {
        return geoName;
    }

    public void setGeoName(String geoName) {
        this.geoName = geoName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getSolId() {
        return solId;
    }

    public void setSolId(String solId) {
        this.solId = solId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }
}

