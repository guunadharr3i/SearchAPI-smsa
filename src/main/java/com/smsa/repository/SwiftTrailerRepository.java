package com.smsa.repository;

import com.smsa.entity.SwiftMessageTrailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftTrailerRepository extends JpaRepository<SwiftMessageTrailer,Long >{
    
}
