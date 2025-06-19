/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;

/**
 *
 * @author abcom
 */

import com.smsa.DTO.SwiftMessageHeaderPojo;
import com.smsa.entity.SwiftMessageHeader;
import com.smsa.repository.SwiftMessageHeaderRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportTxtService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SwiftMessageExportTxtService.class);

    @Autowired
    private SwiftMessageHeaderRepository repository;
    
    @Autowired
    private SwiftMessageService swiftMessageService;

    public File exportTxtZip(String baseDirPath,SwiftMessageHeaderPojo filters) throws IOException {
        log.info("Starting export of SwiftMessageHeaders to TXT and ZIP format...");

        List<SwiftMessageHeaderPojo> records = swiftMessageService.getFilteredMessages(filters);
        if (records.isEmpty()) {
            log.warn("No SwiftMessageHeader records found.");
            return null;
        }
        log.info("Total records to process: {}", records.size());

        File tempDir = new File(baseDirPath, "txt_chunks");
        if (!tempDir.exists()) {
            boolean created = tempDir.mkdirs();
            if (created) {
                log.info("Created temporary directory: {}", tempDir.getAbsolutePath());
            }
        }

        int maxBytes = 10 * 1024; // 10KB
        List<StringBuilder> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (SwiftMessageHeaderPojo record : records) {
            String formatted = formatRecord(record);
            byte[] lineBytes = formatted.getBytes(StandardCharsets.UTF_8);

            if (currentChunk.toString().getBytes(StandardCharsets.UTF_8).length + lineBytes.length > maxBytes) {
                chunks.add(currentChunk);
                currentChunk = new StringBuilder();
            }

            currentChunk.append(formatted).append("\n");
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk);
        }

        log.info("Divided data into {} text file chunks.", chunks.size());

        // Write .txt files
        List<File> txtFiles = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            File file = new File(tempDir, "swift_data_" + (i + 1) + ".txt");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(chunks.get(i).toString().getBytes(StandardCharsets.UTF_8));
                txtFiles.add(file);
                log.debug("Written chunk to file: {}", file.getName());
            }
        }

        // Create ZIP
        File zipFile = new File(baseDirPath, "swift_txt_export.zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (File file : txtFiles) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                    log.debug("Added {} to ZIP", file.getName());
                }
            }
        }
        log.info("ZIP file created at: {}", zipFile.getAbsolutePath());

        // Clean up temp .txt files
        for (File file : txtFiles) {
            if (file.delete()) {
                log.debug("Deleted temporary file: {}", file.getName());
            }
        }

        if (tempDir.delete()) {
            log.debug("Deleted temporary directory: {}", tempDir.getAbsolutePath());
        }

        log.info("Export completed successfully.");
        return zipFile;
    }

    private String formatRecord(SwiftMessageHeaderPojo h) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter inputTimeParser = DateTimeFormatter.ofPattern("HHmmss");
        DateTimeFormatter outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String dateStr = "";
        String timeStr = "";

        if (h.getFileDate() != null) {
            dateStr = h.getFileDate().format(dateFormatter);
        }

        if (h.getTime() != null && !h.getTime().isEmpty()) {
            try {
                LocalTime parsedTime = LocalTime.parse(h.getTime(), inputTimeParser);
                timeStr = parsedTime.format(outputTimeFormatter);
            } catch (DateTimeParseException e) {
                log.warn("Invalid time format '{}' for record ID {}. Using raw value.", h.getTime(), h.getMessageId());
                timeStr = h.getTime();
            }
        }

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

//        if (h.getInstanceRaw() != null && !h.getInstanceRaw().trim().isEmpty()) {
//            sb.append(h.getInstanceRaw().trim()).append("\n");
//        }
//
//        if (h.getHeaderRaw() != null && !h.getHeaderRaw().trim().isEmpty()) {
//            sb.append(h.getHeaderRaw().trim()).append("\n");
//        }

//        sb.append("-----------------Message Text -------------------\n");
//
//        if (h.getRawMessageData() != null && !h.getRawMessageData().trim().isEmpty()) {
//            String cleanedMessageText = extractMessageTextSection(h.getRawMessageData());
//            if (!cleanedMessageText.trim().isEmpty()) {
//                sb.append(cleanedMessageText).append("\n");
//            }
//        }

        sb.append("------------------------------------\n");
        return sb.toString();
    }

    private String safe(Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof LocalDateTime) {
            return ((LocalDateTime) o).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (o instanceof LocalDate) {
            return ((LocalDate) o).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (o instanceof LocalTime) {
            return ((LocalTime) o).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return o.toString().replace("\n", " ").replace("\r", " ");
    }

    private String extractMessageTextSection(String raw) {
        if (raw == null) {
            return "";
        }

        String startMarker = "--------------------------- Message Text ---------------------------";
        String endMarker = "--------------------------- Message Trailer ------------------------";

        int startIndex = raw.indexOf(startMarker);
        if (startIndex == -1) {
            return "";
        }

        int endIndex = raw.indexOf(endMarker, startIndex);
        if (endIndex == -1) {
            return "";
        }

        String between = raw.substring(startIndex + startMarker.length(), endIndex);
        String[] lines = between.split("\r?\n");
        int start = 0, end = lines.length - 1;

        while (start <= end && lines[start].trim().isEmpty()) start++;
        while (end >= start && lines[end].trim().isEmpty()) end--;

        StringBuilder cleaned = new StringBuilder();
        for (int i = start; i <= end; i++) {
            cleaned.append(lines[i]).append("\n");
        }

        return cleaned.toString();
    }
}

