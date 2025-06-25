package com.smsa.entity;


import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SMSA_MSG_GEO")
public class SwiftGeo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "swift_message_geo_gen")
    @SequenceGenerator(name = "swift_message_geo_gen", sequenceName = "swift_message_geo", allocationSize = 1)
    @Column(name = "SMSA_MESSAGE_ID")
    private Long id;


    @Column(name = "SMSA_GEO_ID")
    private String geoId;



    @Column(name = "SMSA_FILE_NAME")
    private String fileName;



    @Column(name = "SMSA_GEOPHY_ID")
    private String geographyId;


    @Column(name = "SMSA_FILE_DATE")
    private LocalDate fileDate;


    /**
     * get field @Column(name = "SMSA_MESSAGE_ID")
     *
     * @return id @Column(name = "SMSA_MESSAGE_ID")

     */
    public Long getId() {
        return this.id;
    }

    /**
     * set field @Column(name = "SMSA_MESSAGE_ID")
     *
     * @param id @Column(name = "SMSA_MESSAGE_ID")

     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get field @Column(name = "SMSA_GEO_ID")
     *
     * @return geoId @Column(name = "SMSA_GEO_ID")

     */
    public String getGeoId() {
        return this.geoId;
    }

    /**
     * set field @Column(name = "SMSA_GEO_ID")
     *
     * @param geoId @Column(name = "SMSA_GEO_ID")

     */
    public void setGeoId(String geoId) {
        this.geoId = geoId;
    }

    /**
     * get field @Column(name = "SMSA_FILE_NAME")
     *
     * @return fileName @Column(name = "SMSA_FILE_NAME")

     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * set field @Column(name = "SMSA_FILE_NAME")
     *
     * @param fileName @Column(name = "SMSA_FILE_NAME")

     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * get field @Column(name = "SMSA_GEOPHY_ID")
     *
     * @return geographyId @Column(name = "SMSA_GEOPHY_ID")

     */
    public String getGeographyId() {
        return this.geographyId;
    }

    /**
     * set field @Column(name = "SMSA_GEOPHY_ID")
     *
     * @param geographyId @Column(name = "SMSA_GEOPHY_ID")

     */
    public void setGeographyId(String geographyId) {
        this.geographyId = geographyId;
    }

    /**
     * get field @Column(name = "SMSA_FILE_DATE")
     *
     * @return fileDate @Column(name = "SMSA_FILE_DATE")

     */
    public LocalDate getFileDate() {
        return this.fileDate;
    }

    /**
     * set field @Column(name = "SMSA_FILE_DATE")
     *
     * @param fileDate @Column(name = "SMSA_FILE_DATE")

     */
    public void setFileDate(LocalDate fileDate) {
        this.fileDate = fileDate;
    }
}
