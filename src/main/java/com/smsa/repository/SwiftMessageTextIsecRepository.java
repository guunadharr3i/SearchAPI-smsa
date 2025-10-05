/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.smsa.repository;

import com.smsa.entity.SwiftMessageTextIsec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// public class SwiftMessageTextRepository {
    
// }
@Repository
public interface SwiftMessageTextIsecRepository extends JpaRepository<SwiftMessageTextIsec, Long> {}
