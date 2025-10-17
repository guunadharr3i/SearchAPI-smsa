package com.smsa.Service;

import com.aspose.cells.*;
import com.smsa.DTO.SmsaDownloadResponsePojo;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class SwiftMessageExcelExportService {

    private static final Logger log = LogManager.getLogger(SwiftMessageExcelExportService.class);

    /**
     * Generates a single binary Excel (.xlsb) workbook with all filtered
     * headers.
     */
    public byte[] exportSwiftHeadersToSingleExcel(SwiftMessageHeaderFilterPojo filters, SmsaDownloadService swiftMessageService) throws IOException {
        log.info("Starting SwiftMessageHeader single Excel (.xlsb) export...");

        List<SmsaDownloadResponsePojo> headers = swiftMessageService.filterDownloadData(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Returning empty Excel.");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Workbook workbook = new Workbook(FileFormatType.XLSB);
            Worksheet sheet = workbook.getWorksheets().get(0);
            sheet.setName("Swift Headers");

            String[] headersRow = createHeaderRow(sheet);

            int rowNum = 1;
            int serialNo = 1;
            for (SmsaDownloadResponsePojo h : headers) {
                populateSheetRow(sheet, rowNum++, h, serialNo++);
            }

            // Set column widths (instead of autoSize, which is slower)
            for (int col = 0; col < headersRow.length; col++) {
                sheet.getCells().setColumnWidth(col, 20); // approx width
            }

            workbook.save(out, SaveFormat.XLSB);
            log.info("Excel (.xlsb) generation completed with {} records.", headers.size());
            return out.toByteArray();
        } catch (Exception ex) {
            log.error("Exception occured :" + ex);
        }
        return null;
    }

    // ---------- helpers -----------------------------------------------------
    private String[] createHeaderRow(Worksheet sheet) {
        String[] headers = {
            "SerialNo", "Identifier", "Sender", "Receiver", "Message Type",
            "Reference No", "Related Ref No", "Send/Rec Date", "Send/Rec Time",
            "ValueDate", "Currency", "Amount", "M_Text", "M_History",
            "File Type A/N", "Send-Rec DateTime", "Unit", "MIR/MOR"
        };

        Cells cells = sheet.getCells();
        for (int i = 0; i < headers.length; i++) {
            cells.get(0, i).putValue(headers[i]);
        }

        return headers;
    }

    private void populateSheetRow(Worksheet sheet, int rowNum, SmsaDownloadResponsePojo h, int serialNo) {
        Cells cells = sheet.getCells();
        int col = 0;
        cells.get(rowNum, col++).putValue(serialNo);
        cells.get(rowNum, col++).putValue(safe(h.getInpOut()));
        cells.get(rowNum, col++).putValue(safe(h.getSenderBic()));
        cells.get(rowNum, col++).putValue(safe(h.getReceiverBic()));
        cells.get(rowNum, col++).putValue(safe(h.getMsgType()));
        cells.get(rowNum, col++).putValue(safe(h.getTransactionRef()));
        cells.get(rowNum, col++).putValue(safe(h.getTransactionRelatedRefNo()));
        cells.get(rowNum, col++).putValue(safe(h.getFileDate()));
        cells.get(rowNum, col++).putValue(safe(h.getFileTime()));
        cells.get(rowNum, col++).putValue(""); // ValueDate placeholder
        cells.get(rowNum, col++).putValue(safe(h.getCurrency()));
        cells.get(rowNum, col++).putValue(safe(h.getTransactionAmount()));
        cells.get(rowNum, col++).putValue(safe(h.getmText()));
        cells.get(rowNum, col++).putValue(""); // M_History placeholder
        cells.get(rowNum, col++).putValue(safe(h.getFileType()));
        cells.get(rowNum, col++).putValue(safe(h.getFileDate() + "," + h.getFileTime()));
        cells.get(rowNum, col++).putValue(""); // Unit placeholder
        cells.get(rowNum, col++).putValue(safe(h.getMiorRef()));
    }

    // ---------- utility -----------------------------------------------------
    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
