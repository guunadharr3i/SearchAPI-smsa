package com.smsa.entity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;



@Entity
@Table(name = "SMSA_MENU_MST")
public class MenuMst {

    @Id
    @Column(name = "Menu_id", nullable = false)
    private Long menuId;

    @Column(name = "Menu_Name", length = 30)
    private String menuName;

    @Column(name = "Menu_Status", length = 1)
    private String menuStatus;

    @Column(name = "Created_date")
    private LocalDateTime createdDate;

    @Column(name = "Remarks", length = 150)
    private String remarks;

    // Getters and Setters

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}