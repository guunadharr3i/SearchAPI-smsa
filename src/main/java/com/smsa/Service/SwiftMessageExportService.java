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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SwiftMessageExportService.class);

    @Autowired
    private SwiftMessageService swiftMessageService;

    /**
     * Stream Excel files directly into a ZIP output stream
     */
    public void streamSwiftHeadersToZip(ZipOutputStream zos, SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Streaming SwiftMessageHeader export directly to ZIP...");

        List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);
        if (headers == null || headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Skipping export.");
            return;
        }

        int rowsPerFile = 1_000_000; // XLSX row limit
        int fileCount = 1;

        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SwiftMessageHeaderPojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            String fileName = "swift_headers_" + (fileCount++) + ".xlsx";

            zos.putNextEntry(new ZipEntry(fileName));

            try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) { // keep 100 rows in memory
                Sheet sheet = workbook.createSheet("SwiftHeaders");
                createHeaderRow(sheet);

                int rowNum = 1;
                for (SwiftMessageHeaderPojo header : chunk) {
                    Row row = sheet.createRow(rowNum++);
                    populateSheetRow(row, header);
                }

                workbook.write(zos); // write directly to ZIP
                workbook.dispose();  // clean temp files
            }

            zos.closeEntry();
            log.info("Added {} rows into {}", chunk.size(), fileName);
        }
    }

    /**
     * Create header row
     */
    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Message Id");
        headerRow.createCell(1).setCellValue("Sender");
        headerRow.createCell(2).setCellValue("Receiver");
        headerRow.createCell(3).setCellValue("Currency");
        headerRow.createCell(4).setCellValue("Inp Out");
        headerRow.createCell(5).setCellValue("UETR");
        headerRow.createCell(6).setCellValue("File Date");
        headerRow.createCell(7).setCellValue("File Type");
        headerRow.createCell(8).setCellValue("Message Type");
        headerRow.createCell(9).setCellValue("Transaction Ref");
        headerRow.createCell(10).setCellValue("File Name");
        headerRow.createCell(11).setCellValue("Transaction Amount");
    }

    /**
     * Populate a row with SwiftMessageHeaderPojo data
     */
    private void populateSheetRow(Row row, SwiftMessageHeaderPojo h) {
        row.createCell(0).setCellValue(safeLong(h.getMessageId()));
        row.createCell(1).setCellValue(safe(h.getSenderBic()));
        row.createCell(2).setCellValue(safe(h.getReceiverBic()));
        row.createCell(3).setCellValue(safe(h.getCurrency()));
        row.createCell(4).setCellValue(safe(h.getInpOut()));
        row.createCell(5).setCellValue(safe(h.getUetr()));
        row.createCell(6).setCellValue(safe(h.getFileDate()));
        row.createCell(7).setCellValue(safe(h.getFileType()));
        row.createCell(8).setCellValue(safe(h.getMsgType()));
        row.createCell(9).setCellValue(safe(h.getTransactionRef()));
        row.createCell(10).setCellValue(safe(h.getFileName()));
        row.createCell(11).setCellValue(safe(h.getTransactionAmount()));
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
