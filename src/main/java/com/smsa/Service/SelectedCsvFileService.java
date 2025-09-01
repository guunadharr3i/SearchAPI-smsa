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

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            // Write Header Row
            writer.write(String.join(",", new String[]{
                "MESSAGE ID",
                "SENDER BIC",
                "RECEIVER BIC",
                "CURRENCY",
                "TRANSACTION AMOUNT",
                "INP OUT",
                "UETR",
                "FILE DATE",
                "FILE TYPE",
                "MESSAGE TYPE",
                "TRANSACTION REF",
                "FILE NAME",}));

            writer.write("\n");

            // Write Data Rows
            for (SwiftMessageHeaderPojo h : headers) {
                String[] row = new String[]{
                    safe(h.getMessageId()),
                    safe(h.getSenderBic()),
                    safe(h.getReceiverBic()),
                    safe(h.getCurrency()),
                    safe(h.getTransactionAmount()),
                    safe(h.getInpOut()),
                    safe(h.getUetr()),
                    safe(h.getFileDate()),
                    safe(h.getFileType()),
                    safe(h.getMsgType()),
                    safe(h.getTransactionRef()),
                    safe(h.getFileName())

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
