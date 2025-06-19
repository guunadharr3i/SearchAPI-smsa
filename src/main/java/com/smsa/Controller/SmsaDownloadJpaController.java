/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Controller;

import com.smsa.Service.SwiftMessageCsvExportService;
import com.smsa.Service.SwiftMessageExportPdfService;
import com.smsa.Service.SwiftMessageExportService;
import com.smsa.Service.SwiftMessageExportTxtService;
import com.smsa.Service.TxtFilesService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author abcom
 */
public class SmsaDownloadJpaController {

    private static final Logger logger = LogManager.getLogger(SmsaDownloadJpaController.class);
    
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


    @GetMapping("/export/xls")
    public ResponseEntity<InputStreamResource> exportSwiftHeadersToExcel() {
        logger.info("Exporting data to Excel zip");
        try {
            String path = System.getProperty("java.io.tmpdir");
            String filePath = exportService.exportSwiftHeadersToZip(path);
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

    @GetMapping("/export/txt")
    public ResponseEntity<InputStreamResource> exportTxtZip() throws IOException {
        logger.info("Exporting TXT zip");
        File zipFile = txtService.exportTxtZip(System.getProperty("java.io.tmpdir"));

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
    public ResponseEntity<InputStreamResource> exportSwiftHeadersToCsv() {
        logger.info("Exporting CSV zip");
        try {
            String path = System.getProperty("java.io.tmpdir");
            String filePath = csvExportService.exportSwiftHeadersToZip(path);
            File zipFile = new File(filePath);

            if (!zipFile.exists()) {
                logger.warn("CSV zip not found at {}", filePath);
                return ResponseEntity.status(404).build();
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
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public String hello() {
        logger.info("Health check endpoint hit");
        return "Dashboard Application Deployed in Server";
    }
}
