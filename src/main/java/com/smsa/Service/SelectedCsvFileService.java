package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SwiftMessageHeaderPojo;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SelectedCsvFileService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SelectedCsvFileService.class);

    @Autowired
    private SwiftMessageService swiftMessageService;

    public byte[] exportSwiftHeadersToCsv(SwiftMessageHeaderFilterPojo filters) {
        log.info("Starting SwiftMessageHeader CSV export...");

        List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            // Write Header Row
            writer.write(String.join(",", new String[]{
                "SMSA_MESSAGE_ID", "SMSA_FILE_NAME", "SMSA_DATE", "SMSA_TIME", "SMSA_MT_CODE",
                "SMSA_PAGE", "SMSA_PRIORITY", "SMSA_FILE_TYPE", "SMSA_INPUT_REF_NO", "SMSA_OUTPUT_REF_NO",
                "SMSA_MSG_IO", "SMSA_MSG_DESC", "SMSA_MSG_TYPE", "SMSA_SLA_ID", "SMSA_SENDER_BIC",
                "SMSA_SENDER_BIC_DESC", "SMSA_RECEIVER_BIC", "SMSA_RECEIVER_BIC_DESC", "SMSA_USER_REF",
                "SMSA_TXN_REF", "SMSA_FILE_DATE", "SMSA_MUR", "SMSA_UETR", "SMSA_TXN_AMOUNT", "SMSA_TXN_RESULT",
                "SMSA_PRIMARY_FMT", "SMSA_SECONDARY_FMT", "SMSA_MSG_CURRENCY"
            }));
            writer.write("\n");

            // Write Data Rows
            for (SwiftMessageHeaderPojo h : headers) {
                String[] row = new String[]{
                    safe(h.getMessageId()),
                    safe(h.getFileName()),
                    safe(h.getDate()),
                    safe(h.getTime()),
                    safe(h.getMtCode()),
                    safe(h.getPage()),
                    safe(h.getPriority()),
                    safe(h.getFileType()),
                    safe(h.getInputRefNo()),
                    safe(h.getOutputRefNo()),
                    safe(h.getInpOut()),
                    safe(h.getMsgDesc()),
                    safe(h.getMsgType()),
                    safe(h.getSlaId()),
                    safe(h.getSenderBic()),
                    safe(h.getSenderBicDesc()),
                    safe(h.getReceiverBic()),
                    safe(h.getReceiverBicDesc()),
                    safe(h.getUserRef()),
                    safe(h.getTransactionRef()),
                    safe(h.getFileDate()),
                    safe(h.getMur()),
                    safe(h.getUetr()),
                    safe(h.getTransactionAmount()),
                    safe(h.getTransactionResult()),
                    safe(h.getPrimaryFormat()),
                    safe(h.getSecondaryFormat()),
                    safe(h.getCurrency())
                };
                writer.write(String.join(",", escapeCsv(row)));
                writer.write("\n");
            }

            writer.flush();
            log.info("CSV generation completed with {} records.", headers.size());
            return out.toByteArray();

        } catch (Exception e) {
            log.error("CSV export failed", e);
            return new byte[0];
        }
    }

    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String[] escapeCsv(String[] values) {
        String[] escaped = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String val = values[i];
            if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
                val = "\"" + val.replace("\"", "\"\"") + "\"";
            }
            escaped[i] = val;
        }
        return escaped;
    }
}
