package com.smsa.Service;

import com.smsa.DTO.SmsaDownloadResponsePojo;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SmsaDownloadResponsePojo;
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
    private SmsaDownloadService swiftMessageService;

    /**
     * Generates a single .xls workbook (byte[]) with all filtered headers.
     */
    public byte[] exportSwiftHeadersToSingleExcel(SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Starting SwiftMessageHeader single Excel export...");

        List<SmsaDownloadResponsePojo> headers = swiftMessageService.filterDownloadData(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Returning empty Excel.");
        }

        // --- HSSFWorkbook instead of XSSFWorkbook ---
        try (Workbook workbook = new HSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Swift Headers");
            createHeaderRow(sheet);

            int rowNum = 1;
            for (SmsaDownloadResponsePojo h : headers) {
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
