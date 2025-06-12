package com.smsa.repository;

import com.smsa.entity.SwiftMessageHeader;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SwiftMessageHeaderRepository extends JpaRepository<SwiftMessageHeader, String> {

    @Query("SELECT s.inpOut, COUNT(s) FROM SwiftMessageHeader s GROUP BY s.inpOut")
    List<Object[]> countBySmsaMsgIoGroup();
}
