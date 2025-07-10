package com.smsa.Service;

import com.smsa.DTO.BicCountryPojo;
import com.smsa.DTO.GeoResponsePojo;
import com.smsa.entity.BicCountry;
import com.smsa.repository.BicCountryRepository;
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

    @Autowired
    private BicCountryRepository bicCountryRepository;

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
        Map<String, BicCountryPojo> map = new HashMap<>();
        try {
            logger.info("Fetching BIC country data...");
            List<BicCountry> bicData = bicCountryRepository.findAll();

            for (BicCountry b : bicData) {
                BicCountryPojo pojo = new BicCountryPojo();
                pojo.setId(b.getId());
                pojo.setCountryCode(b.getCountryCode());
                pojo.setCountryName(b.getCountryName());
                pojo.setLatitude(b.getLatitude());
                pojo.setLongitude(b.getLongitude());

                map.put(b.getCountryCode(), pojo);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching BIC country data: ", e);
        }
        return map;
    }
}
