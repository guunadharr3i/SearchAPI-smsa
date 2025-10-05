package com.smsa.repository;

import com.smsa.entity.SwiftMessageTrailerIsec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftTrailerIsecRepository extends JpaRepository<SwiftMessageTrailerIsec,Long >{
    
}

