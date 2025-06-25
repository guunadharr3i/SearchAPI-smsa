package com.smsa.Controller;



import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.Enums.ApiResponseCode;
import com.smsa.NameConstants.AppConstants;
import com.smsa.ResponseWrappers.DownloadApiResponse;
import com.smsa.Service.*;
import com.smsa.tokenValidation.AuthenticateAPi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SmsaDownloadJpaController {

    private static final Logger logger = LogManager.getLogger(SmsaDownloadJpaController.class);


    @Autowired
    private SwiftMessageExportService exportService;
    @Autowired
    private SwiftMessageExportTxtService txtService;
    @Autowired
    private SwiftMessageCsvExportService csvExportService;
    @Autowired
    private AuthenticateAPi authenticateApi;

    @PostMapping("/download")
    public ResponseEntity<?> getFilteredMessages(
            @RequestBody FilterRequest filter,
            @RequestParam String downloadType) {

        logger.info("Received request to /download with filter: {} and downloadType: {}", filter, downloadType);

        try {
            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenRequest());
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(DownloadApiResponse.error(ApiResponseCode.INVALID_TOKEN));
            }
            switch (downloadType.toUpperCase()) {
                case "XLSX":
                    return exportSwiftHeadersToExcel(filter.getFilter());
                case "TXT":
                    return exportTxtZip(filter.getFilter());
                case "CSV":
                    return exportSwiftHeadersToCsv(filter.getFilter());
                default:
                    return ResponseEntity.badRequest()
                            .body(DownloadApiResponse.error(ApiResponseCode.INVALID_DOWNLOAD_TYPE));
            }

        } catch (Exception e) {
            logger.error("Exception while processing download request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> exportSwiftHeadersToExcel(SwiftMessageHeaderPojo filters) {
        logger.info("Exporting data to Excel zip");
        try {
            String path = System.getProperty(AppConstants.JAVA_IO_TMPDIR);
            String filePath = exportService.exportSwiftHeadersToZip(path, filters);
            File zipFile = new File(filePath);

            if (!zipFile.exists()) {
                logger.warn("Excel export zip file not found at {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            return getFileDownloadResponse(zipFile, "swift_xls_export.zip");
        } catch (IOException e) {
            logger.error("Failed to export Excel zip", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> exportTxtZip(SwiftMessageHeaderPojo filters) {
        logger.info("Exporting TXT zip");
        try {
            File zipFile = txtService.exportTxtZip(System.getProperty(AppConstants.JAVA_IO_TMPDIR), filters);

            if (zipFile == null || !zipFile.exists()) {
                logger.warn("TXT zip export returned null or file not found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.NO_RECORDS));
            }

            return getFileDownloadResponse(zipFile, zipFile.getName());
        } catch (IOException e) {
            logger.error("Error exporting TXT zip", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }
    

    @GetMapping("/export/csv")
    public ResponseEntity<?> exportSwiftHeadersToCsv(SwiftMessageHeaderPojo filters) {
        logger.info("Exporting CSV zip");
        try {
            String path = System.getProperty(AppConstants.JAVA_IO_TMPDIR);
            String filePath = csvExportService.exportSwiftHeadersToZip(path, filters);

            if (filePath == null || filePath.isEmpty()) {
                logger.warn("CSV zip generation failed: No records found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DownloadApiResponse.error(ApiResponseCode.NO_RECORDS));
            }

            File zipFile = new File(filePath);
            if (!zipFile.exists()) {
                logger.warn("CSV zip not found at {}", filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            return getFileDownloadResponse(zipFile, "swift_csv_export.zip");
        } catch (IOException e) {
            logger.error("Error exporting CSV zip", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    private ResponseEntity<InputStreamResource> getFileDownloadResponse(File zipFile, String fileName) throws IOException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
