/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.repository;

/**
 *
 * @author abcom
 */
import com.smsa.entity.BicCountry;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BicCountryRepository extends JpaRepository<BicCountry, Long> {

    // Find by country code
    Optional<BicCountry> findByCountryCode(String countryCode);

    // Find by country name (exact match)
    Optional<BicCountry> findByCountryName(String countryName);

    // Find countries where name contains a keyword (case-insensitive)
    List<BicCountry> findByCountryNameIgnoreCaseContaining(String keyword);

    // Custom query using JPQL (if needed)
    @Query("SELECT b FROM BicCountry b WHERE b.latitude > :latitude")
    List<BicCountry> findCountriesAboveLatitude(@Param("latitude") BigDecimal latitude);
}

