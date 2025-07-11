package com.smsa.Service;

import com.smsa.DTO.BicCountryPojo;
import com.smsa.DTO.GeoResponsePojo;
import com.smsa.repository.SwiftMessageHeaderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class to handle geo-related operations.
 */
@Service
public class GeoDataService {

    @Autowired
    private SwiftMessageHeaderRepository repository;


    private static final Logger logger = LogManager.getLogger(GeoDataService.class);

    public List<GeoResponsePojo> getGeoData() {
        List<GeoResponsePojo> geoData = new ArrayList<>();
        try {
            logger.info("Fetching sender-receiver country transaction data...");
            List<Object[]> countryPaymentsData = repository.getSenderReceiverCountryCountsRaw();

            Map<String, BicCountryPojo> bicMap = getBicCountryData();

            for (Object[] row : countryPaymentsData) {
                GeoResponsePojo g = new GeoResponsePojo();
                g.setSenderCountry((String) row[0]);
                g.setRecieverCountry((String) row[1]);
                g.setLabel(row[0] + " to " + row[1]);
                g.setPaymentsCount(row[2].toString());
                g.setBicData(bicMap.get(g.getSenderCountry()));
                geoData.add(g);
            }

        } catch (Exception e) {
            logger.error("Exception occurred while fetching geo data: ", e);
            // Return an empty list instead of null to avoid NullPointerException at caller
            return Collections.emptyList();
        }
        return geoData;
    }

    public Map<String, BicCountryPojo> getBicCountryData() {
        Map<String, BicCountryPojo> countryPaymentMap = new HashMap<>();

        countryPaymentMap.put("BH", new BicCountryPojo("BH", "Bahrain", 26.0667, 50.5577));
        countryPaymentMap.put("BE", new BicCountryPojo("BE", "Belgium", 50.8503, 4.3517));
        countryPaymentMap.put("CA", new BicCountryPojo("CA", "Canada", 56.1304, -106.3468));
        countryPaymentMap.put("AE", new BicCountryPojo("AE", "Dubai", 25.276987, 55.296249));
        countryPaymentMap.put("DE", new BicCountryPojo("DE", "Germany", 51.1657, 10.4515));
        countryPaymentMap.put("HK", new BicCountryPojo("HK", "Hong Kong", 22.3193, 114.1694));
        countryPaymentMap.put("IG", new BicCountryPojo("IG", "IBUGIFT", 0.0, 0.0)); // Placeholder
        countryPaymentMap.put("IN", new BicCountryPojo("IN", "India", 20.5937, 78.9629));
        countryPaymentMap.put("IS", new BicCountryPojo("IS", "INDIA-SFMS", 21.0, 78.0)); // Placeholder
        countryPaymentMap.put("QA", new BicCountryPojo("QA", "QATAR", 25.276987, 51.5200));
        countryPaymentMap.put("RU", new BicCountryPojo("RU", "Russia", 61.5240, 105.3188));
        countryPaymentMap.put("CN", new BicCountryPojo("CN", "SHANGHAI", 31.2304, 121.4737));
        countryPaymentMap.put("SG", new BicCountryPojo("SG", "Singapore", 1.3521, 103.8198));
        countryPaymentMap.put("ZA", new BicCountryPojo("ZA", "SOUTH AFRICA", -30.5595, 22.9375));
        countryPaymentMap.put("LK", new BicCountryPojo("LK", "SriLanka", 7.8731, 80.7718));
        countryPaymentMap.put("GB", new BicCountryPojo("GB", "UK", 54.0, -2.0));
        countryPaymentMap.put("US", new BicCountryPojo("US", "USA", 38.0, -97.0));

        return countryPaymentMap;
    }
}
