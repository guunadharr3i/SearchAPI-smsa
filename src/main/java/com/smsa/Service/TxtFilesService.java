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
import com.smsa.DTO.SmsaDownloadResponsePojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

@Service
public class TxtFilesService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TxtFilesService.class);


    public File exportSelectedMessagesToTxt(SwiftMessageHeaderFilterPojo filters, String tempDirPath,SmsaDownloadService swiftMessageService) throws IOException {

        List<SmsaDownloadResponsePojo> records = swiftMessageService.filterDownloadData(filters);
        if (records == null || records.isEmpty()) {
            logger.warn("No Swift messages found for provided transaction references.");
            return null;
        }

        File txtFile = new File(tempDirPath, "swift_messages_" + System.currentTimeMillis() + ".txt");
        logger.info("Creating file at: {}", txtFile.getAbsolutePath());

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(txtFile), StandardCharsets.UTF_8))) {

            for (SmsaDownloadResponsePojo header : records) {
                logger.debug("Writing record with transactionRef: {}", header.getTransactionRef());
                writeRecord(header, writer);
                writer.newLine(); // extra space between records
                writer.flush();   // flush periodically for safety
            }

            logger.info("Successfully wrote {} records to TXT file.", records.size());
        } catch (IOException e) {
            logger.error("Error occurred while writing Swift messages to TXT file.", e);
            throw e;
        }

        return txtFile;
    }

    /**
     * Writes a single record directly to the writer (streaming).
     */
    private void writeRecord(SmsaDownloadResponsePojo h, BufferedWriter writer) throws IOException {
        writer.write("------------------------------------\n");
        writer.write("Identifier :- " + safe(h.getInpOut()) + "\n");
        writer.write("Message Type :- " + safe(h.getMsgType()) + "\n");
        writer.write("Sender :- " + safe(h.getSenderBic()) + "\n");
        writer.write("Receiver :- " + safe(h.getReceiverBic()) + "\n");
        writer.write("Send\\Receive Date :- " + safe(h.getFileDate()) + "\n");
        writer.write("Send\\Receive Time :- " + safe(h.getFileTime()) + "\n");
        writer.write("File Type :- " + safe(h.getFileType()) + "\n");
        writer.write("Text :- \n");
        writer.write(safe(h.getmText()));
        writer.write("\n\n\n");
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
