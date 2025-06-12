package com.smsa.repository;

import com.smsa.entity.SwiftMessageIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface SwiftInterventionRepository extends JpaRepository<SwiftMessageIntervention, Long> {}
