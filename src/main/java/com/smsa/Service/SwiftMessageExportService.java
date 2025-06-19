/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;

/**
 *
 * @author abcom
 */
import com.smsa.entity.SwiftMessageHeader;
import com.smsa.repository.SwiftMessageHeaderRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private SwiftMessageHeaderRepository repository;

    public String exportSwiftHeadersToZip(String folderPath) throws IOException {
        log.info("Starting SwiftMessageHeader export...");

        List<SwiftMessageHeader> headers = repository.findAll();
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Aborting export.");
            return null;
        }

        int estimatedRowSize = estimateRowSize(headers.get(0)) + 500;
        int maxFileSizeBytes = 150 * 1024; // 150KB
        int rowsPerFile = Math.max(1, (int) (maxFileSizeBytes * 0.9 / estimatedRowSize));
        int totalFiles = (int) Math.ceil(headers.size() / (double) rowsPerFile);

        log.info("Total records found: {}", headers.size());
        log.info("Estimated row size: {} bytes", estimatedRowSize);
        log.info("Rows per file: {}", rowsPerFile);
        log.info("Expected number of XLSX files: {}", totalFiles);

        File tempDir = new File(folderPath, "temp_xls_" + System.currentTimeMillis());
        if (!tempDir.exists()) tempDir.mkdirs();
        log.info("Created temporary directory: {}", tempDir.getAbsolutePath());

        int fileCount = 1;
        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SwiftMessageHeader> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            Workbook workbook = createWorkbookWithHeaders();
            Sheet sheet = workbook.getSheetAt(0);

            int rowNum = 1;
            for (SwiftMessageHeader header : chunk) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(safeLong(header.getId()));
                row.createCell(1).setCellValue(safe(header.getFileName()));
                row.createCell(2).setCellValue(safe(header.getDate()));
                row.createCell(3).setCellValue(safe(header.getTime()));
                row.createCell(4).setCellValue(safeInt(header.getMtCode()));
                row.createCell(5).setCellValue(safeInt(header.getPage()));
                row.createCell(6).setCellValue(safe(header.getRawMessageData()));
                row.createCell(7).setCellValue(safe(header.getInstanceRaw()));
                row.createCell(8).setCellValue(safe(header.getHeaderRaw()));
                row.createCell(9).setCellValue(safe(header.getPriority()));
                row.createCell(10).setCellValue(safe(header.getFileType()));
                row.createCell(11).setCellValue(safe(header.getSmsaHeaderObj()));
                row.createCell(12).setCellValue(safe(header.getInputRefNo()));
                row.createCell(13).setCellValue(safe(header.getOutputRefNo()));
                row.createCell(14).setCellValue(safe(header.getInpOut()));
                row.createCell(15).setCellValue(safe(header.getMsgDesc()));
                row.createCell(16).setCellValue(safe(header.getMsgType()));
                row.createCell(17).setCellValue(safe(header.getSlaId()));
                row.createCell(18).setCellValue(safe(header.getSenderBic()));
                row.createCell(19).setCellValue(safe(header.getSenderObj()));
                row.createCell(20).setCellValue(safe(header.getSenderBicDesc()));
                row.createCell(21).setCellValue(safe(header.getReceiverobj()));
                row.createCell(22).setCellValue(safe(header.getReceiverBic()));
                row.createCell(23).setCellValue(safe(header.getReceiverBicDesc()));
                row.createCell(24).setCellValue(safe(header.getUserRef()));
                row.createCell(25).setCellValue(safe(header.getTransactionRef()));
                row.createCell(26).setCellValue(safe(header.getFileDate()));
                row.createCell(27).setCellValue(safe(header.getMur()));
                row.createCell(28).setCellValue(safe(header.getUetr()));
            }

            for (int col = 0; col < 29; col++) {
                sheet.autoSizeColumn(col);
            }

            File file = new File(tempDir, "swift_headers_" + fileCount++ + ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                log.info("Created XLSX file: {} with {} rows", file.getName(), chunk.size());
            } finally {
                workbook.close();
            }
        }

        String zipFilePath = folderPath + "/swift_headers_export.zip";
        log.info("Zipping files into: {}", zipFilePath);

        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = tempDir.listFiles((dir, name) -> name.endsWith(".xlsx"));
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

        log.info("ZIP creation complete. Cleaning up temporary files...");
        File[] tempFiles = tempDir.listFiles();
        if (tempFiles != null) {
            for (File f : tempFiles) {
                if (f.delete()) {
                    log.debug("Deleted temp file: {}", f.getName());
                }
            }
        }
        if (tempDir.delete()) {
            log.debug("Deleted temp directory: {}", tempDir.getAbsolutePath());
        }

        log.info("Export process completed successfully. ZIP Path: {}", zipFilePath);
        return zipFilePath;
    }

    private Workbook createWorkbookWithHeaders() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Swift Headers");

        String[] headers = {
            "SMSA_MESSAGE_ID", "SMSA_FILE_NAME", "SMSA_DATE", "SMSA_TIME", "SMSA_MT_CODE",
            "SMSA_PAGE", "SMSA_RAW_DATA", "SMSA_INST_RAW", "SMSA_HDR_RAW", "SMSA_PRIORITY",
            "SMSA_FILE_TYPE", "SMSA_HDR_TEXT", "SMSA_INPUT_REF_NO", "SMSA_OUTPUT_REF_NO",
            "SMSA_MSG_IO", "SMSA_MSG_DESC", "SMSA_MSG_TYPE", "SMSA_SLA_ID", "SMSA_SENDER_BIC",
            "SMSA_SENDER_OBJ", "SMSA_SENDER_BIC_DESC", "SMSA_RECEIVER_OBJ", "SMSA_RECEIVER_BIC",
            "SMSA_RECEIVER_BIC_DESC", "SMSA_USER_REF", "SMSA_TXN_REF", "SMSA_FILE_DATE",
            "SMSA_MUR", "SMSA_UETR"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
        return workbook;
    }

    private int estimateRowSize(SwiftMessageHeader h) {
        String raw = safeLong(h.getId()) + safe(h.getFileName()) + safe(h.getDate()) + safe(h.getTime()) + safeInt(h.getMtCode()) +
                     safeInt(h.getPage()) + safe(h.getRawMessageData()) + safe(h.getInstanceRaw()) + safe(h.getHeaderRaw()) + safe(h.getPriority()) +
                     safe(h.getFileType()) + safe(h.getSmsaHeaderObj()) + safe(h.getInputRefNo()) + safe(h.getOutputRefNo()) +
                     safe(h.getInpOut()) + safe(h.getMsgDesc()) + safe(h.getMsgType()) + safe(h.getSlaId()) + safe(h.getSenderBic()) +
                     safe(h.getSenderObj()) + safe(h.getSenderBicDesc()) + safe(h.getReceiverobj()) + safe(h.getReceiverBic()) +
                     safe(h.getReceiverBicDesc()) + safe(h.getUserRef()) + safe(h.getTransactionRef()) + safe(h.getFileDate()) +
                     safe(h.getMur()) + safe(h.getUetr());
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

