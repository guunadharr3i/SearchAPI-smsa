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
import com.smsa.DTO.SmsaDownloadResponsePojo;

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
    private SmsaDownloadService swiftMessageService;

    /**
     * Stream Excel files directly into a ZIP output stream
     */
    public void streamSwiftHeadersToZip(ZipOutputStream zos, SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Streaming SwiftMessageHeader export directly to ZIP...");

        List<SmsaDownloadResponsePojo> headers = swiftMessageService.filterDownloadData(filters);
        if (headers == null || headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Skipping export.");
            return;
        }

        int rowsPerFile = 1_000_000; // XLSX row limit
        int fileCount = 1;

        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SmsaDownloadResponsePojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            String fileName = "swift_headers_" + (fileCount++) + ".xlsx";

            zos.putNextEntry(new ZipEntry(fileName));

            try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) { // keep 100 rows in memory
                Sheet sheet = workbook.createSheet("SwiftHeaders");
                createHeaderRow(sheet);

                int rowNum = 1;
                for (SmsaDownloadResponsePojo header : chunk) {
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
        String[] headers = {
            "SerialNo",
            "Identifier",
            "Sender",
            "Receiver",
            "Message Type",
            "Reference No",
            "Related Ref No",
            "Send/Rec Date",
            "Send/Rec Time",
            "ValueDate",
            "Currency",
            "Amount",
            "M_Text",
            "M_History",
            "File Type A/N",
            "Send-Rec DateTime",
            "Unit",
            "MIR/MOR"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    /**
     * Populate a row with SmsaDownloadResponsePojo data
     */
    private void populateSheetRow(Row row, SmsaDownloadResponsePojo h) {
        row.createCell(0).setCellValue(1);
        row.createCell(1).setCellValue(safe(h.getInpOut()));
        row.createCell(2).setCellValue(safe(h.getSenderBic()));
        row.createCell(3).setCellValue(safe(h.getReceiverBic()));
        row.createCell(4).setCellValue(safe(h.getMsgType()));
        row.createCell(5).setCellValue(safe(h.getTransactionRef()));
        row.createCell(6).setCellValue(safe(h.getTransactionRelatedRefNo()));
        row.createCell(7).setCellValue(safe(h.getFileDate()));
        row.createCell(8).setCellValue(safe(h.getFileTime()));
        row.createCell(9).setCellValue(" ");
        row.createCell(10).setCellValue(safe(h.getCurrency()));
        row.createCell(11).setCellValue(safe(h.getTransactionAmount()));
        row.createCell(12).setCellValue(safe(h.getmText()));
        row.createCell(13).setCellValue("");
        row.createCell(14).setCellValue(safe(h.getFileType()));
        row.createCell(15).setCellValue(safe(h.getFileDate() + "," + h.getFileTime()));
        row.createCell(16).setCellValue(" ");
        row.createCell(17).setCellValue(safe(h.getMiorRef()));
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
