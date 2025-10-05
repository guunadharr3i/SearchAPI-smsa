/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.repository;

import com.smsa.entity.GeographyUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SwiftGeoMasterRepository_1 extends JpaRepository<GeographyUser, Long> {

    @Query(value = "SELECT GEOG_CODE "
            + "FROM SMSA_GEO_MST "
            + "WHERE GEOG_NAME IN (:geoNames)",
            nativeQuery = true)
    List<String> findGeoCodesByGeoNames(@Param("geoNames") List<String> geoNames);

}
