package com.smsa.Controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.smsa.DTO.FilterRequest;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.Enums.ApiResponseCode;
import com.smsa.NameConstants.AppConstants;
import com.smsa.ResponseWrappers.DownloadApiResponse;
import com.smsa.Service.SelectedCsvFileService;
import com.smsa.Service.SwiftMessageCsvExportService;
import com.smsa.Service.SwiftMessageExcelExportService;
import com.smsa.Service.SwiftMessageExportPdfService;
import com.smsa.Service.SwiftMessageExportService;
import com.smsa.Service.SwiftMessageExportTxtService;
import com.smsa.Service.TxtFilesService;
import com.smsa.Utils.EncryptedtPayloadRequest;
import com.smsa.encryption.AESUtil;
import com.smsa.tokenValidation.AuthenticateAPi;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Autowired
    private SwiftMessageExportPdfService pdfExportService;
    @Autowired
    private TxtFilesService txtFilesService;
    @Autowired
    private SwiftMessageExcelExportService exportSelectedxlsxService;
    @Autowired
    private SelectedCsvFileService exportSelectedCSVService;

    @Value("${aes.auth.key}")
    private String secretKey;
    @Value("${aes.auth.vi.key}")
    private String viKey;

    @PostMapping("/download")
    public ResponseEntity<?> getFilteredMessages(
            @RequestBody EncryptedtPayloadRequest encryptedRequest,
            @RequestParam String downloadType) {
        logger.info("Inside GetFIltered messages method");
        logger.info("Selected downloadType: " + downloadType);

        try {
            // Step 1: Decrypt incoming payload
            String decryptedJson = AESUtil.decrypt(encryptedRequest.getEncryptedPayload(), secretKey, viKey);
            logger.info("DecryptedJson: " + decryptedJson);
            // Step 2: Convert decrypted JSON to FilterRequest
            ObjectMapper mapper = getCustomMapper();
            FilterRequest filter = mapper.readValue(decryptedJson, FilterRequest.class);
            // Step 3: Authentication
//            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenRequest());
//            if (accessToken == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(DownloadApiResponse.error(ApiResponseCode.INVALID_TOKEN));
//            }
            switch (downloadType.toUpperCase()) {
//                case "XLSX":
//                    return exportSwiftHeadersToExcel(filter.getFilter());
                case "TXT":
                    return exportTxtZip(filter.getFilter());
                case "CSV":
                    return exportSwiftHeadersToCsv(filter.getFilter());
                case "PDF":
                    return exportSelectedMessagesToPdf(filter.getFilter());
                case "CTXT":
                    return downloadTxt(filter.getFilter());
                case "CCSV":
                    return selectedDataToCsv(filter.getFilter());
                case "CXLSX":
                    return selectedSwiftHeadersToExcel(filter.getFilter());
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

    @PostMapping("/download/XLSX")
    public ResponseEntity<StreamingResponseBody> getFilteredMessagesXL(
            @RequestBody EncryptedtPayloadRequest encryptedRequest,
            @RequestParam String downloadType) {

        logger.info("Inside GetFiltered messages method");
        logger.info("Selected downloadType: {}", downloadType);

        try {
            // Decrypt and process payload
            String decryptedJson = AESUtil.decrypt(encryptedRequest.getEncryptedPayload(), secretKey, viKey);
            logger.info("DecryptedJson: {}", decryptedJson);

            ObjectMapper mapper =getCustomMapper();
            FilterRequest filter = mapper.readValue(decryptedJson, FilterRequest.class);

            // Authentication (commented out)
//            String accessToken = authenticateApi.validateAndRefreshToken(filter.getTokenRequest());
//            if (accessToken == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }

            switch (downloadType.toUpperCase()) {
                case "XLSX":
                    return exportSwiftHeadersToExcels(filter.getFilter());
                // Add other cases that return StreamingResponseBody
                default:
                    // Return error as StreamingResponseBody or change method signature
                    return createErrorResponse("Invalid download type: " + downloadType);
            }

        } catch (JsonProcessingException e) {
            logger.error("JSON processing error while processing download request", e);
            return createErrorResponse("Invalid request format");
        } catch (Exception e) {
            logger.error("Exception while processing download request", e);
            return createErrorResponse("Internal server error");
        }
    }

    private ResponseEntity<StreamingResponseBody> createErrorResponse(String message) {
        StreamingResponseBody errorBody = outputStream -> {
            try {
                outputStream.write(message.getBytes());
            } catch (IOException e) {
                logger.error("Error writing error response", e);
            }
        };
        return ResponseEntity.badRequest().body(errorBody);
    }

    public ResponseEntity<?> exportSwiftHeadersToExcel(SwiftMessageHeaderFilterPojo filters) {
        logger.info("Exporting data to Excel zip");
        try {
            String path = System.getProperty(AppConstants.JAVA_IO_TMPDIR);
            String filePath = exportService.exportSwiftHeadersToZip(path, filters);
            logger.info("after getting out from service with file path: " + filePath);
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
        } catch (Exception e) {
            logger.error("Failed to export Excel zip", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<StreamingResponseBody> exportSwiftHeadersToExcels(SwiftMessageHeaderFilterPojo filters) {
        logger.info("Exporting data to Excel zip");
        try {
            String path = System.getProperty(AppConstants.JAVA_IO_TMPDIR);
            String filePath = exportService.exportSwiftHeadersToZip(path, filters);
            logger.info("after getting out from service with file path: " + filePath);
            File zipFile = new File(filePath);

            if (!zipFile.exists()) {
                logger.warn("Excel export zip file not found at {}", filePath);
                return createErrorResponse("");
            }

            return getFileDownloadResponse1(zipFile, "swift_xls_export.zip");
        } catch (Exception e) {
            logger.error("Failed to export Excel zip", e);
            // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            //         .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    public ResponseEntity<?> exportTxtZip(SwiftMessageHeaderFilterPojo filters) {
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
    public ResponseEntity<?> exportSwiftHeadersToCsv(SwiftMessageHeaderFilterPojo filters) {
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

    public ResponseEntity<?> exportSelectedMessagesToPdf(SwiftMessageHeaderFilterPojo filters) {
        logger.info("Request received to export selected messages to PDF. Filters: {}", filters);

        File filePath = null;
        try {
            String path = System.getProperty(AppConstants.JAVA_IO_TMPDIR);
            logger.info("Temporary directory for PDF export: {}", path);

            filePath = pdfExportService.exportSelectedMessagesToPdf(path, filters);

            if (filePath == null || !filePath.exists()) {
                logger.warn("PDF generation failed: No records found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DownloadApiResponse.error(ApiResponseCode.NO_RECORDS));
            }

            logger.info("PDF file generated successfully: {}", filePath.getAbsolutePath());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.getName())
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(filePath.length())
                    .body(resource);

        } catch (IOException e) {
            logger.error("IOException while generating or reading PDF file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.FILE_ERROR));
        } catch (Exception e) {
            logger.error("Unexpected error occurred in exportSelectedMessagesToPdf method", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> downloadTxt(SwiftMessageHeaderFilterPojo filters) {
        logger.info("Request received to download TXT with filters: {}", filters);

        File txtFile = null;
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            logger.info("Temporary directory for TXT file: {}", tempDir);

            txtFile = txtFilesService.exportSelectedMessagesToTxt(filters, tempDir);

            if (txtFile == null || !txtFile.exists()) {
                logger.warn("TXT generation failed: No records found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DownloadApiResponse.error(ApiResponseCode.NO_RECORDS));
            }

            logger.info("TXT file generated successfully: {}", txtFile.getAbsolutePath());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(txtFile));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + txtFile.getName())
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);

        } catch (IOException e) {
            logger.error("Error occurred while generating or reading the TXT file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.FILE_ERROR));
        } catch (Exception e) {
            logger.error("Unexpected error in downloadTxt method", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    private ResponseEntity<InputStreamResource> getFileDownloadResponse(File zipFile, String fileName) throws IOException {
        logger.info("inside getFIleDownloadRepone Method  with file name :" + fileName + " file path: " + zipFile.getAbsolutePath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private ResponseEntity<StreamingResponseBody> getFileDownloadResponse1(File zipFile, String fileName) {
        logger.info("Inside getFileDownloadResponse Method with file name: {} file path: {}",
                fileName, zipFile.getAbsolutePath());

        StreamingResponseBody body = outputStream -> {
            try (FileInputStream fis = new FileInputStream(zipFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                logger.info("Successfully streamed file: {}", fileName);
            } catch (IOException e) {
                logger.error("Error streaming file: {}", fileName, e);
                throw new RuntimeException("File streaming failed", e);
            } finally {
                // Always attempt cleanup
                deleteFileWithRetry(zipFile);
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(zipFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(body);
    }

    private void deleteFileWithRetry(File file) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            if (file.delete()) {
                logger.info("Deleted zip file after streaming: {}", file.getAbsolutePath());
                return;
            }
            if (i < maxRetries - 1) {
                try {
                    Thread.sleep(100); // Brief pause before retry
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        logger.warn("Failed to delete zip file after {} attempts: {}", maxRetries, file.getAbsolutePath());
    }

    public Map<String, String> encryptTOken(FilterRequest filter) {
        Map<String, String> refreshCall = new HashMap<>();
        try {
            String encryptedToken = AESUtil.encrypt(filter.getTokenRequest().get("token"), secretKey, viKey);
            refreshCall.put("token", encryptedToken);
            refreshCall.put("DeviceHash", filter.getTokenRequest().get("DeviceHash"));
        } catch (Exception e) {
            return new HashMap();
        }
        return refreshCall;
    }

    public ResponseEntity<?> selectedSwiftHeadersToExcel(@RequestBody SwiftMessageHeaderFilterPojo filters) {
        logger.info("Exporting data to single Excel file");
        try {
            byte[] excelData = exportSelectedxlsxService.exportSwiftHeadersToSingleExcel(filters);

            if (excelData == null || excelData.length == 0) {
                logger.warn("Generated Excel file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.xls");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            logger.error("Failed to export Excel file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }

    public ResponseEntity<?> selectedDataToCsv(@RequestBody SwiftMessageHeaderFilterPojo filters) {
        logger.info("Exporting data to CSV file");

        try {
            byte[] csvData = exportSelectedCSVService.exportSwiftHeadersToCsv(filters);

            if (csvData.length == 0) {
                logger.warn("Generated CSV file is empty");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(DownloadApiResponse.error(ApiResponseCode.FILE_NOT_FOUND));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "swift_headers.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvData);
        } catch (Exception e) {
            logger.error("Failed to export CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DownloadApiResponse.error(ApiResponseCode.INTERNAL_ERROR));
        }
    }
    private ObjectMapper getCustomMapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule customDatesModule = new SimpleModule();

        // Serializer for LocalDate (same as before)
        customDatesModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString()); // yyyy-MM-dd
            }
        });

        // Updated deserializer for LocalDate to handle blank strings gracefully
        customDatesModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String dateStr = p.getValueAsString();
                if (dateStr == null || dateStr.trim().isEmpty()) {
                    return null; // treat blank or empty string as null
                }
                return LocalDate.parse(dateStr.trim());
            }
        });

        // Serializer for LocalDateTime (same as before)
        customDatesModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString()); // yyyy-MM-ddTHH:mm:ss
            }
        });

        // Updated deserializer for LocalDateTime to handle blank strings gracefully
        customDatesModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String dateTimeStr = p.getValueAsString();
                if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
                    return null; // treat blank or empty string as null
                }
                return LocalDateTime.parse(dateTimeStr.trim());
            }
        });

        mapper.registerModule(customDatesModule);
        return mapper;
    }
}
