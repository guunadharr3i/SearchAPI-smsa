package com.smsa.Controller;

import com.smsa.Service.GeoDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geo")  // Base path for geo-related endpoints
public class GeoGraphyController {

    private static final Logger logger = LogManager.getLogger(GeoGraphyController.class);

    @Autowired
    private GeoDataService service;

    @PostMapping("/fetchGeoData")
    public ResponseEntity<?> getGeoData() {
        logger.info("Fetching Geo Data...");
        try {
            Object geoData = service.getGeoData();
            return ResponseEntity.ok(geoData);
        } catch (Exception e) {
            logger.error("Error while fetching geo data", e);
            return ResponseEntity
                    .badRequest()
                    .body("An error occurred while fetching geo data. Please try again later.");
        }
    }
}
