//package com.example.authentication.Entity;
//
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "SMSA_GEO_MST")
//public class GeographyUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_seq")
//    @SequenceGenerator(name = "geo_seq", sequenceName = "SMSA_GEOGRAPHY_SEQ", allocationSize = 1)
//    @Column(name = "GEOG_ID")
//    private Long geogId;
//
//    @Column(name = "GEOG_CODE", nullable = false, length = 50)
//    private String geogCode;
//
//    @Column(name = "GEOG_NAME", nullable = false, length = 100)
//    private String geogName;
//
//    // Constructors
//    public GeographyUser() {}
//
//    public GeographyUser(Long geogId, String geogCode, String geogName) {
//        this.geogId = geogId;
//        this.geogCode = geogCode;
//        this.geogName = geogName;
//    }
//
//    // Getters and Setters
//    public Long getGeogId() {
//        return geogId;
//    }
//
//    public void setGeogId(Long geogId) {
//        this.geogId = geogId;
//    }
//
//    public String getGeogCode() {
//        return geogCode;
//    }
//
//    public void setGeogCode(String geogCode) {
//        this.geogCode = geogCode;
//    }
//
//    public String getGeogName() {
//        return geogName;
//    }
//
//    public void setGeogName(String geogName) {
//        this.geogName = geogName;
//    }
//}
