/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;

/**
 *
 * @author abcom
 */
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SwiftMessageHeaderPojo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SwiftMessageExportService.class);

    @Autowired
    private SwiftMessageService swiftMessageService;

    public String exportSwiftHeadersToZip(String folderPath, SwiftMessageHeaderFilterPojo filters) {
        try {
            log.info("Starting SwiftMessageHeader export...");

            List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);
            if (headers == null || headers.isEmpty()) {
                log.warn("No SwiftMessageHeader records found. Aborting export.");
                return null;
            }

            int estimatedRowSize = estimateRowSize(headers.get(0)) + 500;
            int sizeBasedRowLimit = Math.max(1, (int) ((1024 * 1024 * 0.9) / estimatedRowSize));
            int rowsPerFile = Math.min(65530, sizeBasedRowLimit); // .xls max rows

            log.info("Total records: {}, Estimated row size: {}, Rows/file: {}", headers.size(), estimatedRowSize, rowsPerFile);

            File tempDir = new File(folderPath, "temp_xls_" + System.currentTimeMillis());
            tempDir.mkdirs();
            log.info("Temp directory: {}", tempDir.getAbsolutePath());

            writeChunksToXlsxFiles(headers, tempDir, rowsPerFile);
            log.info("after write chunk files method call");

            String zipPath = folderPath + "/swift_headers_export.zip";
            zipFiles(tempDir, zipPath);
//            cleanTempDirectory(tempDir);

            log.info("Export complete: {}", zipPath);
            return zipPath;

        } catch (Exception e) {
            log.error("Error during exportSwiftHeadersToZip: ", e);
            return null;
        }
    }

    private void writeChunksToXlsxFiles(List<SwiftMessageHeaderPojo> headers, File tempDir, int rowsPerFile) throws IOException {
        int fileCount = 1;
        try {
            for (int i = 0; i < headers.size(); i += rowsPerFile) {
                List<SwiftMessageHeaderPojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));

                Workbook workbook = createHssfWorkbookWithHeaders(); // Updated helper
                Sheet sheet = workbook.getSheetAt(0);
                log.info("File count: " + fileCount + " Chunck size: " + chunk.size());
                int rowNum = 1;
                for (SwiftMessageHeaderPojo header : chunk) {
                    Row row = sheet.createRow(rowNum++);
                    populateSheetRow(row, header); // This should work as-is if generic
                }
                log.info("after populating file count: " + fileCount);

                for (int col = 0; col < 34; col++) {
                    sheet.autoSizeColumn(col);
                }
                log.info("before creating file of filecount: " + fileCount);
                File file = new File(tempDir, "swift_headers_" + fileCount++ + ".xls"); // .xls instead of .xlsx
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    log.info("Created XLS: {} with {} rows", file.getName(), chunk.size());
                } finally {
                    workbook.close();
                }
            }
        } catch (Exception e) {
            log.info("Exception araised: " + e);
        }
        log.info("End of method write chunk files with file count: " + fileCount);
    }

    private Workbook createHssfWorkbookWithHeaders() {
        Workbook workbook = new HSSFWorkbook(); // XLS workbook
        Sheet sheet = workbook.createSheet("SwiftHeaders");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Message Id");
        headerRow.createCell(1).setCellValue("Identifier");
        headerRow.createCell(2).setCellValue("Sender");
        headerRow.createCell(3).setCellValue("Receiver");
        headerRow.createCell(4).setCellValue("MT Code");
        headerRow.createCell(5).setCellValue("Date");
        headerRow.createCell(6).setCellValue("Time");
        headerRow.createCell(7).setCellValue("File Type");
        headerRow.createCell(8).setCellValue("Currency");
        headerRow.createCell(9).setCellValue("Amount");
        headerRow.createCell(10).setCellValue("uetr");
        headerRow.createCell(11).setCellValue("Input Ref No");
        headerRow.createCell(12).setCellValue("Output Ref No");
        headerRow.createCell(13).setCellValue("File Name");
        headerRow.createCell(14).setCellValue("Message Desc");
        headerRow.createCell(15).setCellValue("Message Type");
        headerRow.createCell(16).setCellValue("SLA ID");
        headerRow.createCell(17).setCellValue("Priority");
        headerRow.createCell(18).setCellValue("Sender BIC Desc");
        headerRow.createCell(19).setCellValue("Receiver BIC Desc");
        headerRow.createCell(20).setCellValue("User Ref");
        headerRow.createCell(21).setCellValue("Transaction Ref");
        headerRow.createCell(22).setCellValue("File Date");
        headerRow.createCell(23).setCellValue("MUR");
        headerRow.createCell(24).setCellValue("Transaction Result");
        headerRow.createCell(25).setCellValue("Primary FMT");
        headerRow.createCell(26).setCellValue("Secondary FMT");

        // Add up to 34 headers
        return workbook;
    }

    private void populateSheetRow(Row row, SwiftMessageHeaderPojo h) {
        row.createCell(0).setCellValue(safeLong(h.getMessageId()));
        row.createCell(1).setCellValue(safe(h.getInpOut()));
        row.createCell(2).setCellValue(safe(h.getSenderBic()));
        row.createCell(3).setCellValue(safe(h.getReceiverBic()));
        row.createCell(4).setCellValue(safeInt(h.getMtCode()));
        row.createCell(5).setCellValue(safe(h.getDate()));
        row.createCell(6).setCellValue(safe(h.getTime()));
        row.createCell(7).setCellValue(safe(h.getFileType()));
        row.createCell(8).setCellValue(safe(h.getCurrency()));
        row.createCell(9).setCellValue(safe(h.getTransactionAmount()));
        row.createCell(10).setCellValue(safe(h.getUetr()));
        row.createCell(11).setCellValue(safe(h.getInputRefNo()));
        row.createCell(12).setCellValue(safe(h.getOutputRefNo()));
        row.createCell(13).setCellValue(safe(h.getFileName()));
        row.createCell(14).setCellValue(safe(h.getMsgDesc()));
        row.createCell(15).setCellValue(safe(h.getMsgType()));
        row.createCell(16).setCellValue(safe(h.getSlaId()));
        row.createCell(17).setCellValue(safe(h.getPriority()));
        row.createCell(18).setCellValue(safe(h.getSenderBicDesc()));
        row.createCell(19).setCellValue(safe(h.getReceiverBicDesc()));
        row.createCell(20).setCellValue(safe(h.getUserRef()));
        row.createCell(21).setCellValue(safe(h.getTransactionRef()));
        row.createCell(22).setCellValue(safe(h.getFileDate()));
        row.createCell(23).setCellValue(safe(h.getMur()));
        row.createCell(24).setCellValue(safe(h.getTransactionResult()));
        row.createCell(25).setCellValue(safe(h.getPrimaryFormat()));
        row.createCell(26).setCellValue(safe(h.getSecondaryFormat()));
    }

    private void zipFiles(File directory, String zipPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipPath); ZipOutputStream zos = new ZipOutputStream(fos)) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".xls"));
            if (files != null) {
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                        log.info("Added {} to ZIP", file.getName());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void cleanTempDirectory(File tempDir) {
        File[] files = tempDir.listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    Files.delete(f.toPath());
                } catch (IOException e) {
                    log.warn("Failed to delete file: {}", f.getAbsolutePath(), e);
                }
            }
        }
        try {
            Files.delete(tempDir.toPath());
        } catch (IOException e) {
            log.warn("Failed to delete temp directory: {}", tempDir.getAbsolutePath(), e);
        }
    }

//    private Workbook createWorkbookWithHeaders() {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Swift Headers");
//
//        String[] headers = {
//                "Message Id", "Identifier", "Sender", "Receiver", "MT Code",
//                "Date",
//                "Time", "File Type", "Currency", "Amount", "uetr", "Input Ref No", "Output Ref No",
//                "File Name", "Message Desc", "Message Type", "SLA ID", "Priority",
//                "Sender BIC Desc",
//                "Receiver BIC Desc", "User Ref", "Transaction Ref", "File Date",
//                "MUR", "Transaction Result", "Primary FMT", "Secondary FMT"
//        };
//
//        Row headerRow = sheet.createRow(0);
//        for (int i = 0; i < headers.length; i++) {
//            headerRow.createCell(i).setCellValue(headers[i]);
//        }
//        return workbook;
//    }
    private int estimateRowSize(SwiftMessageHeaderPojo h) {
        String raw = safeLong(h.getMessageId()) + safe(h.getInpOut()) + safe(h.getSenderBic()) + safe(h.getReceiverBic())
                + safeInt(h.getMtCode())
                + safe(h.getDate())
                + safe(h.getTime()) + safe(h.getFileType()) + safe(h.getCurrency())
                + safe(h.getTransactionAmount()) + safe(h.getUetr()) + safe(h.getInputRefNo()) + safe(h.getOutputRefNo())
                + safe(h.getFileName())
                + safe(h.getMsgDesc()) + safe(h.getMsgType())
                + safe(h.getSlaId()) + safe(h.getPriority()) + safe(h.getSenderBicDesc())
                + safe(h.getReceiverBicDesc())
                + safe(h.getUserRef()) + safe(h.getTransactionRef()) + safe(h.getFileDate()) + safe(h.getMur())
                + safe(h.getTransactionResult())
                + safe(h.getPrimaryFormat()) + safe(h.getSecondaryFormat());
        return raw.getBytes(StandardCharsets.UTF_8).length;
    }

    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String safeInt(Integer i) {
        return i == null ? "" : i.toString();
    }

    private String safeLong(Long l) {
        return l == null ? "" : l.toString();
    }
}
