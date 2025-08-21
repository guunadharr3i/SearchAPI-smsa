package com.smsa.repository;

import com.smsa.entity.SmsaRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsaRoleRepository extends JpaRepository<SmsaRole, Long> {

    SmsaRole findByRoleName(String roleName);
}
