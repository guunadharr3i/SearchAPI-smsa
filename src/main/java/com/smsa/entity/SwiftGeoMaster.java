//package com.smsa.entity;
//
//import java.io.Serializable;
//import javax.persistence.*;
//
//
//@Entity
//@Table(name = "SMSA_GEO_MST")
//public class SwiftGeoMaster implements Serializable{
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_message_mst_gen")
//    @SequenceGenerator(name = "swift_message_mst_gen", sequenceName = "swift_message_mst", allocationSize = 1)
//    @Column(name = "SMSA_MESSAGE_ID")
//    private Long id;
//
//    @Column(name = "SMSA_GEPHY_ID")
//    private String geoId;
//
//    @Column(name = "SMSA_GEO_NAME")
//    private Long geoName;
//
//    /**
//     * get field @Column(name = "SMSA_MESSAGE_ID")
//     *
//     * @return id @Column(name = "SMSA_MESSAGE_ID")
//
//     */
//    public Long getId() {
//        return this.id;
//    }
//
//    /**
//     * set field @Column(name = "SMSA_MESSAGE_ID")
//     *
//     * @param id @Column(name = "SMSA_MESSAGE_ID")
//
//     */
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    /**
//     * get field @Column(name = "SMSA_GEPHY_ID")
//     *
//     * @return geoId @Column(name = "SMSA_GEPHY_ID")
//
//     */
//    public String getGeoId() {
//        return this.geoId;
//    }
//
//    /**
//     * set field @Column(name = "SMSA_GEPHY_ID")
//     *
//     * @param geoId @Column(name = "SMSA_GEPHY_ID")
//
//     */
//    public void setGeoId(String geoId) {
//        this.geoId = geoId;
//    }
//
//    /**
//     * get field @Column(name = "SMSA_GEO_NAME")
//     *
//     * @return geoName @Column(name = "SMSA_GEO_NAME")
//
//     */
//    public Long getGeoName() {
//        return this.geoName;
//    }
//
//    /**
//     * set field @Column(name = "SMSA_GEO_NAME")
//     *
//     * @param geoName @Column(name = "SMSA_GEO_NAME")
//
//     */
//    public void setGeoName(Long geoName) {
//        this.geoName = geoName;
//    }
//}
