package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;   // <— switched
import org.apache.poi.ss.usermodel.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.Logger;

@Service
public class SwiftMessageExcelExportService {

    private static final Logger log = LogManager.getLogger(SwiftMessageExcelExportService.class);

    @Autowired
    private SwiftMessageService swiftMessageService;

    /**
     * Generates a single .xls workbook (byte[]) with all filtered headers.
     */
    public byte[] exportSwiftHeadersToSingleExcel(SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Starting SwiftMessageHeader single Excel export...");

        List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Returning empty Excel.");
        }

        // --- HSSFWorkbook instead of XSSFWorkbook ---
        try (Workbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Swift Headers");
            createHeaderRow(sheet);

            int rowNum = 1;
            for (SwiftMessageHeaderPojo h : headers) {
                Row row = sheet.createRow(rowNum++);
                populateSheetRow(row, h);
            }

            // Adjust column widths (you can trim 34 if you have fewer columns)
            for (int col = 0; col < 34; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(out);
            log.info("Excel generation completed with {} records.", headers.size());
            return out.toByteArray();
        }
    }

    // ---------- helpers -----------------------------------------------------
    private void createHeaderRow(Sheet sheet) {
        String[] headers = {
            "MESSAGE ID",
            "FILE NAME",
            "DATE",
            "TIME",
            "MT CODE",
            "PRIORITY",
            "SENDER BIC",
            "RECEIVER BIC",
            "TRANSACTION REF",
            "AMOUNT",
            "CURRENCY",
            "TRANSACTION RESULT",
            "RAW MESSAGE TEXT"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void populateSheetRow(Row row, SwiftMessageHeaderPojo h) {
        row.createCell(0).setCellValue(safeLong(h.getMessageId()));       // MESSAGE ID
        row.createCell(1).setCellValue(safe(h.getSenderBic()));           // SENDER BIC
        row.createCell(2).setCellValue(safe(h.getReceiverBic()));         // RECEIVER BIC
        row.createCell(3).setCellValue(safe(h.getCurrency()));            // CURRENCY
        row.createCell(4).setCellValue(safe(h.getTransactionAmount()));   // TRANSACTION AMOUNT
        row.createCell(5).setCellValue(safe(h.getInpOut()));              // INP OUT
        row.createCell(6).setCellValue(safe(h.getUetr()));                // UETR
        row.createCell(7).setCellValue(safe(h.getFileDate()));            // FILE DATE
        row.createCell(8).setCellValue(safe(h.getFileType()));            // FILE TYPE
        row.createCell(9).setCellValue(safe(h.getMsgType()));             // MESSAGE TYPE
        row.createCell(10).setCellValue(safe(h.getTransactionRef()));     // TRANSACTION REF
        row.createCell(11).setCellValue(safe(h.getFileName()));           // FILE NAME
    }

    // ---------- utility -----------------------------------------------------
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
