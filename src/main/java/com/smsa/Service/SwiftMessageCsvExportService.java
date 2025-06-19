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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageCsvExportService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SwiftMessageCsvExportService.class);

    @Autowired
    private SwiftMessageHeaderRepository repository;

    @Autowired
    private SwiftMessageService swiftMessageService;

    public String exportSwiftHeadersToZip(String folderPath, SwiftMessageHeaderPojo filters) throws IOException {
        logger.info("Starting exportSwiftHeadersToZip with folderPath: {}", folderPath);

        List<SwiftMessageHeaderPojo> headers = swiftMessageService.getFilteredMessages(filters);
        logger.info("Fetched {} SwiftMessageHeader records from DB", headers.size());

        if (headers.isEmpty()) {
            logger.warn("No records found to export");
            return null;
        }

        int estimatedRowSize = estimateRowSize(headers.get(0)) + 300;
        int maxFileSizeBytes = 150 * 1024; // 150KB
        int rowsPerFile = Math.max(1, (int) (maxFileSizeBytes * 0.9 / estimatedRowSize));

        logger.info("Estimated row size: {} bytes, rows per file: {}", estimatedRowSize, rowsPerFile);

        File tempDir = new File(folderPath, "temp_csv");
        if (!tempDir.exists() && tempDir.mkdirs()) {
            logger.info("Created temp directory at {}", tempDir.getAbsolutePath());
        }

        int fileCount = 1;
        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SwiftMessageHeaderPojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            File csvFile = new File(tempDir, "swift_headers_" + fileCount++ + ".csv");

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8))) {
                logger.info("Writing file: {}", csvFile.getName());
                writeCsvHeader(writer);
                for (SwiftMessageHeaderPojo h : chunk) {
                    writeCsvRow(writer, h);
                }
            } catch (IOException e) {
                logger.error("Error writing CSV file: {}", csvFile.getName(), e);
                throw e;
            }
        }

        String zipFilePath = folderPath + "/swift_headers_export_csv.zip";
        logger.info("Zipping files into: {}", zipFilePath);

        try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = tempDir.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files != null) {
                for (File file : files) {
                    logger.info("Adding to zip: {}", file.getName());
                    try (FileInputStream fis = new FileInputStream(file)) {
                        zos.putNextEntry(new ZipEntry(file.getName()));
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    } catch (IOException e) {
                        logger.error("Error adding file to zip: {}", file.getName(), e);
                        throw e;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error creating zip file", e);
            throw e;
        }

        logger.info("Cleaning up temp CSV files");
        for (File f : tempDir.listFiles()) {
            boolean deleted = f.delete();
            if (!deleted) {
                logger.warn("Failed to delete file: {}", f.getAbsolutePath());
            }
        }
        boolean dirDeleted = tempDir.delete();
        if (!dirDeleted) {
            logger.warn("Failed to delete temp directory: {}", tempDir.getAbsolutePath());
        }

        logger.info("Export complete: {}", zipFilePath);
        return zipFilePath;
    }

    private void writeCsvHeader(BufferedWriter writer) throws IOException {
        writer.write(String.join(",", new String[]{
            "SMSA_MESSAGE_ID", "SMSA_FILE_NAME", "SMSA_DATE", "SMSA_TIME", "SMSA_MT_CODE",
            "SMSA_PAGE", "SMSA_PRIORITY",
            "SMSA_FILE_TYPE","SMSA_INPUT_REF_NO", "SMSA_OUTPUT_REF_NO",
            "SMSA_MSG_IO", "SMSA_MSG_DESC", "SMSA_MSG_TYPE", "SMSA_SLA_ID", "SMSA_SENDER_BIC",
            "SMSA_SENDER_BIC_DESC", "SMSA_RECEIVER_BIC",
            "SMSA_RECEIVER_BIC_DESC", "SMSA_USER_REF", "SMSA_TXN_REF", "SMSA_FILE_DATE",
            "SMSA_MUR", "SMSA_UETR"
        }));
        writer.newLine();
    }

    private void writeCsvRow(BufferedWriter writer, SwiftMessageHeaderPojo h) throws IOException {
        writer.write(String.join(",", new String[]{
            csv(h.getMessageId()), csv(h.getFileName()), csv(h.getDate()), csv(h.getTime()), csv(h.getMtCode()),
            csv(h.getPage()), csv(h.getPriority()),
            csv(h.getFileType()), csv(h.getInputRefNo()), csv(h.getOutputRefNo()),
            csv(h.getInpOut()), csv(h.getMsgDesc()), csv(h.getMsgType()), csv(h.getSlaId()), csv(h.getSenderBic()),
            csv(h.getSenderBicDesc()), csv(h.getReceiverBic()),
            csv(h.getReceiverBicDesc()), csv(h.getUserRef()), csv(h.getTransactionRef()), csv(h.getFileDate()),
            csv(h.getMur()), csv(h.getUetr())
        }));
        writer.newLine();
    }

    private String csv(Object o) {
        if (o == null) {
            return "";
        }
        String s = o.toString().replace("\"", "\"\"");
        return "\"" + s + "\"";
    }

    private int estimateRowSize(SwiftMessageHeaderPojo h) {
        String raw = String.join("",
                csv(h.getMessageId()), csv(h.getFileName()), csv(h.getDate()), csv(h.getTime()), csv(h.getMtCode()),
                csv(h.getPage()), csv(h.getPriority()),
                csv(h.getFileType()), csv(h.getInputRefNo()), csv(h.getOutputRefNo()),
                csv(h.getInpOut()), csv(h.getMsgDesc()), csv(h.getMsgType()), csv(h.getSlaId()), csv(h.getSenderBic()),
                csv(h.getSenderBicDesc()), csv(h.getReceiverBic()),
                csv(h.getReceiverBicDesc()), csv(h.getUserRef()), csv(h.getTransactionRef()), csv(h.getFileDate()),
                csv(h.getMur()), csv(h.getUetr())
        );
        return raw.getBytes(StandardCharsets.UTF_8).length;
    }
}
