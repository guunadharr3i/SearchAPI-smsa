/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.Service.SwiftMessageCsvExportService;
import com.smsa.Service.SwiftMessageExportPdfService;
import com.smsa.Service.SwiftMessageExportService;
import com.smsa.Service.SwiftMessageExportTxtService;
import com.smsa.Service.SwiftMessageService;
import com.smsa.Service.TxtFilesService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author abcom
 */
@RestController
@RequestMapping
public class SmsaDownloadJpaController {

    private static final Logger logger = LogManager.getLogger(SmsaDownloadJpaController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${authentication.url}")
    private String authenticationUrl;

    @Autowired
    private SwiftMessageExportService exportService;

    @Autowired
    private SwiftMessageExportTxtService txtService;

    @Autowired
    private SwiftMessageExportPdfService pdfExportService;

    @Autowired
    private TxtFilesService txtFilesService;

    @Autowired
    private SwiftMessageCsvExportService csvExportService;

    @Autowired
    private ObjectMapper objectMapper; // Add this if not autowired already

    @Autowired
    private SwiftMessageService service;

    @PostMapping("/download")
    public ResponseEntity<?> getFilteredMessages(
            @RequestBody FilterRequest filter,
            @RequestParam String downloadType) {

        logger.info("Received request to /download with filter: {} and downloadType: {}", filter, downloadType);

        try {
            switch (downloadType.toUpperCase()) {
                case "XLSX":
                    return exportSwiftHeadersToExcel(filter.getFilter());
                case "TXT":
                    return exportTxtZip(filter.getFilter());
                case "CSV":
                    return exportSwiftHeadersToCsv(filter.getFilter());
                default:
                    logger.warn("Invalid downloadType: {}", downloadType);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid downloadType: " + downloadType);
            }
        } catch (Exception e) {
            logger.error("Exception while processing download request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while generating the file.");
        }
    }

    public ResponseEntity<InputStreamResource> exportSwiftHeadersToExcel(SwiftMessageHeaderPojo filters) {
        logger.info("Exporting data to Excel zip");
        try {
            String path = System.getProperty("java.io.tmpdir");
            String filePath = exportService.exportSwiftHeadersToZip(path, filters);
            File zipFile = new File(filePath);

            if (!zipFile.exists()) {
                logger.warn("Excel export zip file not found at {}", filePath);
                return ResponseEntity.status(404).build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            logger.info("Excel zip created: {}", zipFile.getAbsolutePath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=swift_xls_export.zip")
                    .contentLength(zipFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            logger.error("Failed to export Excel zip", e);
            return ResponseEntity.status(500).build();
        }
    }

    public ResponseEntity<InputStreamResource> exportTxtZip(SwiftMessageHeaderPojo filters) throws IOException {
        logger.info("Exporting TXT zip");
        File zipFile = txtService.exportTxtZip(System.getProperty("java.io.tmpdir"), filters);

        if (zipFile == null) {
            logger.warn("TXT zip export returned null");
            return ResponseEntity.noContent().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
        logger.info("TXT zip file generated: {}", zipFile.getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + zipFile.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(zipFile.length())
                .body(resource);
    }

    @PostMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportSelectedMessagesToPdf(@RequestBody List<String> txnRefs)
            throws IOException {
        logger.info("Exporting PDF for selected txnRefs: {}", txnRefs);
        File pdf = pdfExportService.exportSelectedMessagesToPdf(txnRefs, System.getProperty("java.io.tmpdir"));

        if (pdf == null) {
            logger.warn("PDF export returned null");
            return ResponseEntity.noContent().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdf));
        logger.info("PDF exported: {}", pdf.getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdf.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length())
                .body(resource);
    }

    @PostMapping("/selected/txt")
    public ResponseEntity<Resource> downloadTxt(@RequestBody List<String> txnRefs) throws IOException {
        logger.info("Exporting selected TXT for txnRefs: {}", txnRefs);
        File txtFile = txtFilesService.exportSelectedMessagesToTxt(txnRefs, System.getProperty("java.io.tmpdir"));

        if (txtFile == null) {
            logger.warn("TXT export returned null");
            return ResponseEntity.noContent().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(txtFile));
        logger.info("TXT exported: {}", txtFile.getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + txtFile.getName())
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @GetMapping("/export/csv")
public ResponseEntity<?> exportSwiftHeadersToCsv(SwiftMessageHeaderPojo filters) {
    logger.info("Exporting CSV zip");
    try {
        String path = System.getProperty("java.io.tmpdir");
        String filePath = csvExportService.exportSwiftHeadersToZip(path, filters);

        if (filePath == null || filePath.isEmpty()) {
            logger.warn("CSV zip generation failed: No records found.");
            return ResponseEntity.status(404).body("No records found to export.");
        }

        File zipFile = new File(filePath);

        if (!zipFile.exists()) {
            logger.warn("CSV zip not found at {}", filePath);
            return ResponseEntity.status(404).body("Exported file not found.");
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
        logger.info("CSV zip file exported: {}", zipFile.getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=swift_csv_export.zip")
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    } catch (IOException e) {
        logger.error("Error exporting CSV zip", e);
        return ResponseEntity.status(500).body("Internal Server Error while exporting file.");
    }
}

}
