package com.smsa.repository;

import com.smsa.entity.UserLogonHstDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLogonHstRepository  extends JpaRepository<UserLogonHstDtl,Long> {



    List<UserLogonHstDtl> findByUserId(String userId);


    UserLogonHstDtl findByUserIdAndDeviceHash(String userId, String deviceHash);

}
