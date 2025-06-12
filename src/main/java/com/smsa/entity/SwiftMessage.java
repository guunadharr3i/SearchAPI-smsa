package com.smsa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


public class SwiftMessage implements Serializable{
      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long id;
   
    
}
