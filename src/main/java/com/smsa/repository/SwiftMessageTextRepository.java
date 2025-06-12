package com.smsa.repository;

import com.smsa.entity.SwiftMessageText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// public class SwiftMessageTextRepository {
    
// }
@Repository
public interface SwiftMessageTextRepository extends JpaRepository<SwiftMessageText, Long> {}