package com.smsa.Service;

import com.aspose.cells.*;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SmsaDownloadResponsePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class SwiftMessageExportService {

    private static final Logger log = LogManager.getLogger(SwiftMessageExportService.class);

    @Autowired
    @Qualifier("smsa_download")
    private SmsaDownloadService swiftMessageService;

    @Autowired
    @Qualifier("isec_download")
    private SmsaDownloadService isecMessageService;

    /**
     * Stream Excel (.xlsb) files directly into a ZIP output stream
     */
    public void streamSwiftHeadersToZip(ZipOutputStream zos, SwiftMessageHeaderFilterPojo filters, String moduleName) throws IOException {
        log.info("Streaming SwiftMessageHeader export directly to ZIP (XLSB)...");
        List<SmsaDownloadResponsePojo> headers ;
        if (moduleName.equals("SMSA")) {
            headers = swiftMessageService.filterDownloadData(filters);
        } else {
            headers = isecMessageService.filterDownloadData(filters);
        }
        if (headers == null || headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Skipping export.");
            return;
        }

        int rowsPerFile = 1_048_570; // XLSB also supports 1,048,576 rows
        int fileCount = 1;

        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SmsaDownloadResponsePojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            String fileName = "swift_headers_" + (fileCount++) + ".xlsb"; // ✅ XLSB extension

            zos.putNextEntry(new ZipEntry(fileName));

            try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
                Workbook workbook = new Workbook(FileFormatType.XLSB);
                Worksheet sheet = workbook.getWorksheets().get(0);
                sheet.setName("SwiftHeaders");

                // Create header row
                createHeaderRow(sheet);

                int rowNum = 1;
                int serialNo = 1;
                for (SmsaDownloadResponsePojo header : chunk) {
                    populateSheetRow(sheet, rowNum++, header, serialNo++);
                }

                // Set column widths
                for (int col = 0; col < 18; col++) {
                    sheet.getCells().setColumnWidth(col, 20); // ~5000 in POI units
                }

                workbook.save(baos, SaveFormat.XLSB);
                zos.write(baos.toByteArray());
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(SwiftMessageExportService.class.getName()).log(Level.SEVERE, null, ex);
            }

            zos.closeEntry();
            log.info("Added {} rows into {}", chunk.size(), fileName);
        }
    }

    private void createHeaderRow(Worksheet sheet) {
        String[] headers = {
            "SerialNo", "Identifier", "Sender", "Receiver", "Message Type",
            "Reference No", "Related Ref No", "Send/Rec Date", "Send/Rec Time",
            "ValueDate", "Currency", "Amount", "M_Text", "M_History",
            "File Type A/N", "Send-Rec DateTime", "Unit", "MIR/MOR"
        };

        for (int i = 0; i < headers.length; i++) {
            sheet.getCells().get(0, i).putValue(headers[i]);
        }
    }

    private void populateSheetRow(Worksheet sheet, int rowNum, SmsaDownloadResponsePojo h, int serialNo) {
        int col = 0;
        sheet.getCells().get(rowNum, col++).putValue(serialNo);
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getInpOut()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getSenderBic()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getReceiverBic()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getMsgType()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getTransactionRef()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getTransactionRelatedRefNo()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getFileDate()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getFileTime()));
        sheet.getCells().get(rowNum, col++).putValue(""); // ValueDate placeholder
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getCurrency()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getTransactionAmount()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getmText()));
        sheet.getCells().get(rowNum, col++).putValue(""); // M_History placeholder
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getFileType()));
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getFileDate() + "," + h.getFileTime()));
        sheet.getCells().get(rowNum, col++).putValue(""); // Unit placeholder
        sheet.getCells().get(rowNum, col++).putValue(safe(h.getMiorRef()));
    }

    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
