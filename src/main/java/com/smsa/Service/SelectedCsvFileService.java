package com.smsa.Service;

import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SmsaDownloadResponsePojo;
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

    public byte[] exportSwiftHeadersToCsv(SwiftMessageHeaderFilterPojo filters, SmsaDownloadService swiftMessageService) {
        log.info("Starting SwiftMessageHeader Pipe-delimited export...");

        List<SmsaDownloadResponsePojo> headers = swiftMessageService.filterDownloadData(filters);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            // Write Header Row with safe delimiter
            writer.write(String.join("|", new String[]{
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
                "m_text",
                "M_History",
                "File Type A/N",
                "Send-Rec DateTime",
                "Unit",
                "MIR/MOR"
            }));
            writer.write("\n");

            // Write Data Rows
            for (SmsaDownloadResponsePojo h : headers) {
                String[] row = new String[]{
                    safe(1),
                    safe(h.getInpOut()),
                    safe(h.getSenderBic()),
                    safe(h.getReceiverBic()),
                    safe(h.getMsgType()),
                    safe(h.getTransactionRef()),
                    safe(h.getTransactionRelatedRefNo()),
                    safe(h.getFileDate()),
                    safe(h.getFileTime()),
                    safe(" "),
                    safe(h.getCurrency()),
                    safe(h.getTransactionAmount()),
                    safe(h.getmText()),
                    safe(" "),
                    safe(h.getFileType()),
                    safe(h.getFileDate() + "," + h.getFileTime()),
                    safe(" "),
                    safe(h.getMiorRef())
                };

                writer.write(String.join("|", escapePipe(row)));
                writer.write("\n");
            }

            writer.flush();
            log.info("Pipe-delimited file generation completed with {} records.", headers.size());
            return out.toByteArray();

        } catch (Exception e) {
            log.error("Pipe-delimited export failed", e);
            return new byte[0];
        }
    }

    private String safe(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    // Escape only if value contains safe or newline
    private String[] escapePipe(String[] values) {
        String[] escaped = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            String val = values[i];
            if (val.contains("|") || val.contains("\n")) {
                val = "\"" + val.replace("\"", "\"\"") + "\"";
            }
            escaped[i] = val;
        }
        return escaped;
    }
}
