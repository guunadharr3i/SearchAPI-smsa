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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportTxtService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SwiftMessageExportTxtService.class);

   

    public File exportTxtZip(String baseDirPath, SwiftMessageHeaderFilterPojo filters,SmsaDownloadService swiftMessageService) throws IOException {
        log.info("Starting export of SwiftMessageHeaders directly into ZIP...");

        List<SmsaDownloadResponsePojo> records = swiftMessageService.filterDownloadData(filters);
        if (records.isEmpty()) {
            log.warn("No SwiftMessageHeader records found.");
            return null;
        }

        File zipFile = new File(baseDirPath, "swift_txt_export.zip");

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos, StandardCharsets.UTF_8)) {

            int maxBytes = 1024 * 1024; // 1 MB per TXT file
            int currentBytes = 0;
            int fileIndex = 1;

            ZipEntry entry = new ZipEntry("General_Search_Report_" + fileIndex + ".txt");
            zos.putNextEntry(entry);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos, StandardCharsets.UTF_8));

            for (SmsaDownloadResponsePojo header : records) {
                // Write record and calculate size on the fly
                int writtenBytes = writeRecord(header, writer);

                if (currentBytes + writtenBytes > maxBytes) {
                    // close current entry
                    writer.flush();
                    zos.closeEntry();

                    // start a new file in the zip
                    fileIndex++;
                    entry = new ZipEntry("General_Search_Report_" + fileIndex + ".txt");
                    zos.putNextEntry(entry);
                    writer = new BufferedWriter(new OutputStreamWriter(zos, StandardCharsets.UTF_8));

                    currentBytes = 0;
                }

                writer.flush(); // ensure content is flushed before counting
                currentBytes += writtenBytes;
            }

            writer.flush();
            zos.closeEntry();
        }

        log.info("Export completed successfully. ZIP at: {}", zipFile.getAbsolutePath());
        return zipFile;
    }

    /**
     * Write a single record directly into the writer and return its byte size.
     */
    private int writeRecord(SmsaDownloadResponsePojo h, BufferedWriter writer) throws IOException {
        StringBuilder temp = new StringBuilder();

        temp.append("------------------------------------\n");
        temp.append("Identifier :- ").append(safe(h.getInpOut())).append("\n");
        temp.append("Message Type :- ").append(safe(h.getMsgType())).append("\n");
        temp.append("Sender :- ").append(safe(h.getSenderBic())).append("\n");
        temp.append("Receiver  :- ").append(safe(h.getReceiverBic())).append("\n");
        temp.append("Send\\Receive Date :- ").append(safe(h.getFileDate())).append("\n");
        temp.append("Send\\Receive Time :- ").append(safe(h.getFileTime())).append("\n");
        temp.append("File Type :- ").append(safe(h.getFileType())).append("\n");
        temp.append("Text :- \n");
        temp.append(safe(h.getmText()));
        temp.append("\n\n\n");

        String record = temp.toString();
        writer.write(record);

        return record.getBytes(StandardCharsets.UTF_8).length;
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
}
