package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class SwiftMessageExcelExportService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SwiftMessageExcelExportService.class);

    @Autowired
    private SwiftMessageService swiftMessageService;

    public byte[] exportSwiftHeadersToSingleExcel(SwiftMessageHeaderFilterPojo filters) throws IOException {
        log.info("Starting SwiftMessageHeader single Excel export...");

        List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);
        if (headers.isEmpty()) {
            log.warn("No SwiftMessageHeader records found. Returning empty Excel.");
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Swift Headers");
            createHeaderRow(sheet);

            int rowNum = 1;
            for (SwiftMessageHeaderPojo h : headers) {
                Row row = sheet.createRow(rowNum++);
                populateSheetRow(row, h);
            }

            for (int col = 0; col < 34; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(out);
            log.info("Excel generation completed with {} records.", headers.size());
            return out.toByteArray();
        }
    }

    private void createHeaderRow(Sheet sheet) {
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
    }

    private void populateSheetRow(Row row, SwiftMessageHeaderPojo h) {
        row.createCell(0).setCellValue(safeLong(h.getMessageId()));
        row.createCell(1).setCellValue(safe(h.getFileName()));
        row.createCell(2).setCellValue(safe(h.getDate()));
        row.createCell(3).setCellValue(safe(h.getTime()));
        row.createCell(4).setCellValue(safeInt(h.getMtCode()));
        row.createCell(5).setCellValue(safeInt(h.getPage()));
        row.createCell(6).setCellValue(safe(h.getPriority()));
        row.createCell(7).setCellValue(safe(h.getFileType()));
        row.createCell(8).setCellValue(safe(h.getInputRefNo()));
        row.createCell(9).setCellValue(safe(h.getOutputRefNo()));
        row.createCell(10).setCellValue(safe(h.getInpOut()));
        row.createCell(11).setCellValue(safe(h.getMsgDesc()));
        row.createCell(12).setCellValue(safe(h.getMsgType()));
        row.createCell(13).setCellValue(safe(h.getSlaId()));
        row.createCell(14).setCellValue(safe(h.getSenderBic()));
        row.createCell(15).setCellValue(safe(h.getSenderBicDesc()));
        row.createCell(16).setCellValue(safe(h.getReceiverBic()));
        row.createCell(17).setCellValue(safe(h.getReceiverBicDesc()));
        row.createCell(18).setCellValue(safe(h.getUserRef()));
        row.createCell(19).setCellValue(safe(h.getTransactionRef()));
        row.createCell(20).setCellValue(safe(h.getFileDate()));
        row.createCell(21).setCellValue(safe(h.getMur()));
        row.createCell(22).setCellValue(safe(h.getUetr()));
        row.createCell(23).setCellValue(safe(h.getTransactionAmount()));
        row.createCell(24).setCellValue(safe(h.getTransactionResult()));
        row.createCell(25).setCellValue(safe(h.getPrimaryFormat()));
        row.createCell(26).setCellValue(safe(h.getSecondaryFormat()));
        row.createCell(27).setCellValue(safe(h.getCurrency()));
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
