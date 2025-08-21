/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.repository;

/**
 *
 * @author abcom
 */
import com.smsa.entity.UserRoleGeoMappingMST;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleGeoMappingMSTRepository extends JpaRepository<UserRoleGeoMappingMST, Long> {


    UserRoleGeoMappingMST findByUserIdAndDeletedDateIsNull(String userId);


    @Query(
            value = "SELECT * FROM SMSA_USER_ROLE_GEO_MAPPING_MST WHERE USER_ID = :userId AND GEO_ID = :geoId AND MODULE_ID = :moduleId",
            nativeQuery = true
    )
    UserRoleGeoMappingMST findByUserIdAndGeoIdAndModuleId(@Param("userId") String userId, @Param("geoId") Long geoId,@Param("moduleId") Long moduleId);

    @Query(
            value = "SELECT * FROM SMSA_USER_ROLE_GEO_MAPPING_MST WHERE USER_ID = :userId AND GEO_ID = :geoId AND MODULE_ID = :moduleId and Role_Name = :roleName",
            nativeQuery = true
    )
    UserRoleGeoMappingMST findByUserIdAndGeoIdAndModuleIdAndRoleName(@Param("userId") String userId, @Param("geoId") Long geoId,@Param("moduleId") Long moduleId,@Param("roleName") String roleName);

    @Query(
            value = "SELECT * FROM SMSA_USER_ROLE_GEO_MAPPING_MST WHERE USER_ID = :userId AND GEO_ID = :geoId",
            nativeQuery = true
    )
    UserRoleGeoMappingMST findByUserIdAndGeoId(@Param("userId") String userId, @Param("geoId") Long geoId);


}



