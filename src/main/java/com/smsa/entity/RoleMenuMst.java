package com.smsa.entity;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "SMSA_ROLE_MENU_MST")
public class RoleMenuMst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Or SEQUENCE if using Oracle
    @Column(name = "id")
    private Long id;

    @Column(name = "Menu_id", nullable = false)
    private Long menuId;


    @Column(name = "Role_id", nullable = false)
    private Long roleId;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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

