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
import com.smsa.DTO.SwiftMessageHeaderPojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TxtFilesService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TxtFilesService.class);

    @Autowired
    private SwiftMessageService swiftMessageService;

    public File exportSelectedMessagesToTxt(SwiftMessageHeaderFilterPojo filters, String tempDirPath) throws IOException {

        List<SwiftMessageHeaderPojo> records = swiftMessageService.getFilteredMessages(filters);
        if (records == null || records.isEmpty()) {
            logger.warn("No Swift messages found for provided transaction references.");
            return null;
        }

        File txtFile = new File(tempDirPath, "swift_messages_" + System.currentTimeMillis() + ".txt");
        logger.info("Creating file at: {}", txtFile.getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile))) {
            for (SwiftMessageHeaderPojo header : records) {
                logger.debug("Writing record with transactionRef: {}", header.getTransactionRef());
                writer.write(formatRecord(header));
                writer.newLine(); // extra space between records
            }
            logger.info("Successfully wrote {} records to TXT file.", records.size());
        } catch (IOException e) {
            logger.error("Error occurred while writing Swift messages to TXT file.", e);
            throw e;
        }

        return txtFile;
    }

    private String formatRecord(SwiftMessageHeaderPojo h) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        String dateStr = (h.getFileDate() != null) ? h.getFileDate().format(dateFormatter) : "";
        String timeStr = (h.getTime() != null) ? h.getTime() : "";

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------\n");
        sb.append("Identifier :- ").append(safe(h.getInpOut())).append("\n");
        sb.append("Message Type  :- ").append(safe(h.getMtCode())).append("\n");
        sb.append("Sender :- ").append(safe(h.getSenderBic())).append("\n");
        sb.append("Receiver :- ").append(safe(h.getReceiverBic())).append("\n");
        sb.append("Send\\Receive Date :- ").append(dateStr).append("\n");
        sb.append("Send\\Receive Time :- ").append(timeStr).append("\n");
        sb.append("File Type :- ").append(safe(h.getFileType())).append("\n");
        sb.append("Text :- \n");

//        if (notBlank(h.getInstanceRaw()))
//            sb.append(h.getInstanceRaw().trim()).append("\n");
//
//        if (notBlank(h.getHeaderRaw()))
//            sb.append(h.getHeaderRaw().trim()).append("\n");
//
        sb.append("-----------------Message Text -------------------\n");

        if (notBlank(h.getRawTxt())) {
            String cleaned = extractMessageTextSection(h.getRawTxt());
            if (notBlank(cleaned))
                sb.append(cleaned).append("\n");
        }

        sb.append("------------------------------------\n");
        return sb.toString();
    }

    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String safe(Object o) {
        if (o == null) return "";
        if (o instanceof LocalDateTime) return ((LocalDateTime) o).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (o instanceof LocalDate) return ((LocalDate) o).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (o instanceof LocalTime) return ((LocalTime) o).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return o.toString().replace("\n", " ").replace("\r", " ");
    }

    private String extractMessageTextSection(String raw) {
        if (raw == null) return "";

        String startMarker = "--------------------------- Message Text ---------------------------";
        String endMarker = "--------------------------- Message Trailer ------------------------";

        int startIndex = raw.indexOf(startMarker);
        int endIndex = raw.indexOf(endMarker, startIndex);

        if (startIndex == -1 || endIndex == -1) {
            logger.warn("Unable to find message text section markers in raw data.");
            return "";
        }

        String between = raw.substring(startIndex + startMarker.length(), endIndex);
        String[] lines = between.split("\r?\n");
        StringBuilder cleaned = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                cleaned.append(line).append("\n");
            }
        }

        return cleaned.toString();
    }
}

