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
    }

    /**
     * Populate a row with SwiftMessageHeaderPojo data
     */
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
