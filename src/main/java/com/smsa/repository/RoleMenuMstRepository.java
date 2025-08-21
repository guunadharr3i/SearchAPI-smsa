package com.smsa.repository;


import com.smsa.DTO.RoleMenuDTO;
import com.smsa.entity.RoleMenuMst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RoleMenuMstRepository extends JpaRepository<RoleMenuMst, Long> {
    @Query("SELECT sr.roleId AS roleId, sr.roleName AS roleName, m.menuId AS menuId, m.menuName AS menuName " +
            "FROM RoleMenuMst r " +
            "JOIN SmsaRole sr ON r.roleId = sr.roleId " +
            "JOIN MenuMst m ON r.menuId = m.menuId " +
            "WHERE r.roleId = :roleId")
    List<RoleMenuDTO> findRoleMenuDetails(@Param("roleId") Long roleId);
}