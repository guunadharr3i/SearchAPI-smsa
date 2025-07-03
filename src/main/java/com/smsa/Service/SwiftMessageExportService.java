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

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SwiftMessageExportService.class);


    @Autowired
    private SwiftMessageService swiftMessageService;

    public String exportSwiftHeadersToZip(String folderPath, SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Starting SwiftMessageHeader export...");

        List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Aborting export.");
            return null;
        }

        int estimatedRowSize = estimateRowSize(headers.get(0)) + 500;
        int rowsPerFile = Math.max(1, (int) ((1024 * 1024 * 0.9) / estimatedRowSize));
        int totalFiles = (int) Math.ceil(headers.size() / (double) rowsPerFile);

        log.info("Total records: {}, Estimated row size: {}, Rows/file: {}, Files: {}", headers.size(), estimatedRowSize, rowsPerFile, totalFiles);

        File tempDir = new File(folderPath, "temp_xls_" + System.currentTimeMillis());
        tempDir.mkdirs();
        log.info("Temp directory: {}", tempDir.getAbsolutePath());

        writeChunksToXlsxFiles(headers, tempDir, rowsPerFile);

        String zipPath = folderPath + "/swift_headers_export.zip";
        zipFiles(tempDir, zipPath);

        cleanTempDirectory(tempDir);
        log.info("Export complete: {}", zipPath);
        return zipPath;
    }

    private void writeChunksToXlsxFiles(List<SwiftMessageHeaderPojo> headers, File tempDir, int rowsPerFile) throws IOException {
        int fileCount = 1;
        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SwiftMessageHeaderPojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            Workbook workbook = createWorkbookWithHeaders();
            Sheet sheet = workbook.getSheetAt(0);

            int rowNum = 1;
            for (SwiftMessageHeaderPojo header : chunk) {
                Row row = sheet.createRow(rowNum++);
                populateSheetRow(row, header);
            }

            for (int col = 0; col < 34; col++) {
                sheet.autoSizeColumn(col);
            }

            File file = new File(tempDir, "swift_headers_" + fileCount++ + ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                log.info("Created XLSX: {} with {} rows", file.getName(), chunk.size());
            } finally {
                workbook.close();
            }
        }
    }

    private void populateSheetRow(Row row, SwiftMessageHeaderPojo h) {
        row.createCell(0).setCellValue(safeLong(h.getMessageId()));
        row.createCell(1).setCellValue(safe(h.getFileName()));
        row.createCell(2).setCellValue(safe(h.getDate()));
        row.createCell(3).setCellValue(safe(h.getTime()));
        row.createCell(4).setCellValue(safeInt(h.getMtCode()));
        row.createCell(5).setCellValue(safeInt(h.getPage()));
        row.createCell(9).setCellValue(safe(h.getPriority()));
        row.createCell(10).setCellValue(safe(h.getFileType()));
        row.createCell(12).setCellValue(safe(h.getInputRefNo()));
        row.createCell(13).setCellValue(safe(h.getOutputRefNo()));
        row.createCell(14).setCellValue(safe(h.getInpOut()));
        row.createCell(15).setCellValue(safe(h.getMsgDesc()));
        row.createCell(16).setCellValue(safe(h.getMsgType()));
        row.createCell(17).setCellValue(safe(h.getSlaId()));
        row.createCell(18).setCellValue(safe(h.getSenderBic()));
        row.createCell(20).setCellValue(safe(h.getSenderBicDesc()));
        row.createCell(22).setCellValue(safe(h.getReceiverBic()));
        row.createCell(23).setCellValue(safe(h.getReceiverBicDesc()));
        row.createCell(24).setCellValue(safe(h.getUserRef()));
        row.createCell(25).setCellValue(safe(h.getTransactionRef()));
        row.createCell(26).setCellValue(safe(h.getFileDate()));
        row.createCell(27).setCellValue(safe(h.getMur()));
        row.createCell(28).setCellValue(safe(h.getUetr()));
        row.createCell(29).setCellValue(safe(h.getTransactionAmount()));
        row.createCell(30).setCellValue(safe(h.getTransactionResult()));
        row.createCell(31).setCellValue(safe(h.getPrimaryFormat()));
        row.createCell(32).setCellValue(safe(h.getSecondaryFormat()));
        row.createCell(33).setCellValue(safe(h.getCurrency()));
    }

    private void zipFiles(File directory, String zipPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipPath); ZipOutputStream zos = new ZipOutputStream(fos)) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".xlsx"));
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

    private Workbook createWorkbookWithHeaders() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Swift Headers");

        String[] headers = {
            "SMSA_MESSAGE_ID", "SMSA_FILE_NAME", "SMSA_DATE", "SMSA_TIME", "SMSA_MT_CODE",
            "SMSA_PAGE", "SMSA_PRIORITY",
            "SMSA_FILE_TYPE", "SMSA_INPUT_REF_NO", "SMSA_OUTPUT_REF_NO",
            "SMSA_MSG_IO", "SMSA_MSG_DESC", "SMSA_MSG_TYPE", "SMSA_SLA_ID", "SMSA_SENDER_BIC",
            "SMSA_SENDER_BIC_DESC", "SMSA_RECEIVER_BIC",
            "SMSA_RECEIVER_BIC_DESC", "SMSA_USER_REF", "SMSA_TXN_REF", "SMSA_FILE_DATE",
            "SMSA_MUR", "SMSA_UETR", "SMSA_TXN_AMOUNT", "SMSA_TXN_RESULT", "SMSA_PRIMARY_FMT", "SMSA_SECONDARY_FMT",
            "SMSA_MSG_CURRENCY"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        return workbook;
    }

    private int estimateRowSize(SwiftMessageHeaderPojo h) {
        String raw = safeLong(h.getMessageId()) + safe(h.getFileName()) + safe(h.getDate()) + safe(h.getTime()) + safeInt(h.getMtCode())
                + safeInt(h.getPage()) + safe(h.getPriority())
                + safe(h.getFileType()) + safe(h.getInputRefNo()) + safe(h.getOutputRefNo())
                + safe(h.getInpOut()) + safe(h.getMsgDesc()) + safe(h.getMsgType()) + safe(h.getSlaId()) + safe(h.getSenderBic())
                + safe(h.getSenderBicDesc()) + safe(h.getReceiverBic())
                + safe(h.getReceiverBicDesc()) + safe(h.getUserRef()) + safe(h.getTransactionRef()) + safe(h.getFileDate())
                + safe(h.getMur()) + safe(h.getUetr()) + safe(h.getTransactionAmount()) + safe(h.getTransactionResult()) + safe(h.getPrimaryFormat())
                + safe(h.getSecondaryFormat()) + safe(h.getCurrency());
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
