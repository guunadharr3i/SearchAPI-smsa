/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.DTO;

import java.util.List;

/**
 *
 * @author abcom
 */
public class GeoResponsePojo {

    private BicCountryPojo bicData;
    private String senderCountry;
    private String recieverCountry;
    private String label;
    private String paymentsCount;


    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the paymentsCount
     */
    public String getPaymentsCount() {
        return paymentsCount;
    }

    /**
     * @param paymentsCount the paymentsCount to set
     */
    public void setPaymentsCount(String paymentsCount) {
        this.paymentsCount = paymentsCount;
    }

    /**
     * @return the senderCountry
     */
    public String getSenderCountry() {
        return senderCountry;
    }

    /**
     * @param senderCountry the senderCountry to set
     */
    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    /**
     * @return the recieverCountry
     */
    public String getRecieverCountry() {
        return recieverCountry;
    }

    /**
     * @param recieverCountry the recieverCountry to set
     */
    public void setRecieverCountry(String recieverCountry) {
        this.recieverCountry = recieverCountry;
    }

    /**
     * @return the bicData
     */
    public BicCountryPojo getBicData() {
        return bicData;
    }

    /**
     * @param bicData the bicData to set
     */
    public void setBicData(BicCountryPojo bicData) {
        this.bicData = bicData;
    }

}
