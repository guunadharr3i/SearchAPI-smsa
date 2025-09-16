package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SmsaDownloadResponsePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class SwiftMessageExportService {

    private static final Logger log = LogManager.getLogger(SwiftMessageExportService.class);

    @Autowired
    private SmsaDownloadService swiftMessageService;

    /**
     * Stream Excel (.xlsx) files directly into a ZIP output stream
     */
    public void streamSwiftHeadersToZip(ZipOutputStream zos, SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Streaming SwiftMessageHeader export directly to ZIP...");

        List<SmsaDownloadResponsePojo> headers = swiftMessageService.filterDownloadData(filters);
        if (headers == null || headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Skipping export.");
            return;
        }

        int rowsPerFile = 1_000_000; // XLSX limit
        int fileCount = 1;

        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SmsaDownloadResponsePojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            String fileName = "swift_headers_" + (fileCount++) + ".xlsx"; // ✅ correct extension

            zos.putNextEntry(new ZipEntry(fileName));

            try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) { // keeps only 100 rows in memory
                Sheet sheet = workbook.createSheet("SwiftHeaders");
                createHeaderRow(sheet);

                int rowNum = 1;
                int serialNo = 1;
                for (SmsaDownloadResponsePojo header : chunk) {
                    Row row = sheet.createRow(rowNum++);
                    populateSheetRow(row, header, serialNo++);
                }

                // ✅ instead of slow autoSizeColumn → set fixed widths
                for (int col = 0; col < 18; col++) {
                    sheet.setColumnWidth(col, 5000);
                }

                workbook.write(zos);   // write directly to ZIP
                workbook.dispose();    // cleanup temp files
            }

            zos.closeEntry();
            log.info("Added {} rows into {}", chunk.size(), fileName);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        String[] headers = {
                "SerialNo", "Identifier", "Sender", "Receiver", "Message Type",
                "Reference No", "Related Ref No", "Send/Rec Date", "Send/Rec Time",
                "ValueDate", "Currency", "Amount", "M_Text", "M_History",
                "File Type A/N", "Send-Rec DateTime", "Unit", "MIR/MOR"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void populateSheetRow(Row row, SmsaDownloadResponsePojo h, int serialNo) {
        row.createCell(0).setCellValue(serialNo);
        row.createCell(1).setCellValue(safe(h.getInpOut()));
        row.createCell(2).setCellValue(safe(h.getSenderBic()));
        row.createCell(3).setCellValue(safe(h.getReceiverBic()));
        row.createCell(4).setCellValue(safe(h.getMsgType()));
        row.createCell(5).setCellValue(safe(h.getTransactionRef()));
        row.createCell(6).setCellValue(safe(h.getTransactionRelatedRefNo()));
        row.createCell(7).setCellValue(safe(h.getFileDate()));
        row.createCell(8).setCellValue(safe(h.getFileTime()));
        row.createCell(9).setCellValue(""); // ValueDate placeholder
        row.createCell(10).setCellValue(safe(h.getCurrency()));
        row.createCell(11).setCellValue(safe(h.getTransactionAmount()));
        row.createCell(12).setCellValue(safe(h.getmText()));
        row.createCell(13).setCellValue(""); // M_History placeholder
        row.createCell(14).setCellValue(safe(h.getFileType()));
        row.createCell(15).setCellValue(safe(h.getFileDate() + "," + h.getFileTime()));
        row.createCell(16).setCellValue(""); // Unit placeholder
        row.createCell(17).setCellValue(safe(h.getMiorRef()));
    }

    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
