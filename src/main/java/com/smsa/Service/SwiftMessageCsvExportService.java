package com.smsa.Service;

import com.smsa.DTO.SmsaDownloadResponsePojo;
import com.smsa.DTO.SwiftMessageHeaderFilterPojo;
import com.smsa.DTO.SmsaDownloadResponsePojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageCsvExportService {

    private static final org.apache.logging.log4j.Logger logger = LogManager
            .getLogger(SwiftMessageCsvExportService.class);

    @Autowired
    private SmsaDownloadService swiftMessageService;

    public String exportSwiftHeadersToZip(String folderPath, SwiftMessageHeaderFilterPojo filters) throws IOException {
        logger.info("Starting exportSwiftHeadersToZip with folderPath: {}", folderPath);

        List<SmsaDownloadResponsePojo> headers = swiftMessageService.filterDownloadData(filters);
        logger.info("Fetched {} SwiftMessageHeader records from DB", headers.size());

        if (headers.isEmpty()) {
            logger.warn("No records found to export");
            return null;
        }

        int rowsPerFile = calculateRowsPerFile(headers.get(0));
        File tempDir = prepareTempDirectory(folderPath);

        writeCsvFiles(headers, tempDir, rowsPerFile);
        String zipFilePath = folderPath + "/swift_headers_export_csv.zip";
        zipCsvFiles(tempDir, zipFilePath);
        cleanUpTempFiles(tempDir);

        logger.info("Export complete: {}", zipFilePath);
        return zipFilePath;
    }

    private int calculateRowsPerFile(SmsaDownloadResponsePojo sample) {
        int estimatedRowSize = estimateRowSize(sample) + 300;
        int maxFileSizeBytes = 1024 * 1024; // 1MB
        return Math.max(1, (int) (maxFileSizeBytes * 0.9 / estimatedRowSize));
    }

    private File prepareTempDirectory(String folderPath) {
        File tempDir = new File(folderPath, "temp_csv");
        if (!tempDir.exists() && tempDir.mkdirs()) {
            logger.info("Created temp directory at {}", tempDir.getAbsolutePath());
        }
        return tempDir;
    }

    private void writeCsvFiles(List<SmsaDownloadResponsePojo> headers, File tempDir, int rowsPerFile) throws IOException {
        int fileCount = 1;
        for (int i = 0; i < headers.size(); i += rowsPerFile) {
            List<SmsaDownloadResponsePojo> chunk = headers.subList(i, Math.min(i + rowsPerFile, headers.size()));
            File csvFile = new File(tempDir, "General_Search_Report_" + fileCount++ + ".csv");

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8))) {
                logger.info("Writing file: {}", csvFile.getName());
                writeCsvHeader(writer);
                for (SmsaDownloadResponsePojo h : chunk) {
                    writeCsvRow(writer, h);
                }
            } catch (IOException e) {
                logger.error("Error writing CSV file: {}", csvFile.getName(), e);
                throw e;
            }
        }
    }

    private void zipCsvFiles(File tempDir, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            File[] files = tempDir.listFiles((dir, name) -> name.endsWith(".csv"));
            if (files != null) {
                for (File file : files) {
                    addFileToZip(file, zos);
                }
            }
        }
    }

    private void addFileToZip(File file, ZipOutputStream zos) throws IOException {
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

    private void cleanUpTempFiles(File tempDir) {
        logger.info("Cleaning up temp CSV files");

        File[] files = tempDir.listFiles();
        if (files != null) {
            for (File f : files) {
                try {
                    Files.delete(f.toPath());
                } catch (IOException e) {
                    logger.warn("Failed to delete file: {}", f.getAbsolutePath(), e);
                }
            }
        }

        try {
            Files.delete(tempDir.toPath());
        } catch (IOException e) {
            logger.warn("Failed to delete temp directory: {}", tempDir.getAbsolutePath(), e);
        }
    }

    private void writeCsvHeader(BufferedWriter writer) throws IOException {
        writer.write(String.join("|",
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
        ));

        writer.newLine();
    }

    private void writeCsvRow(BufferedWriter writer, SmsaDownloadResponsePojo h) throws IOException {
        writer.write(String.join("|",
                pipe(1),
                pipe(h.getInpOut()),
                pipe(h.getSenderBic()),
                pipe(h.getReceiverBic()),
                pipe(h.getMsgType()),
                pipe(h.getTransactionRef()),
                pipe(h.getTransactionRelatedRefNo()),
                pipe(h.getFileDate()),
                pipe(h.getFileTime()),
                pipe(" "),
                pipe(h.getCurrency()),
                pipe(h.getTransactionAmount()),
                pipe(h.getmText()),
                pipe(" "),
                pipe(h.getFileType()),
                pipe(h.getFileDate()+","+h.getFileTime()),
                pipe(" "),
                pipe(h.getMiorRef())
        ));

        writer.newLine();
    }

    private String pipe(Object o) {
        if (o == null) {
            return "";
        }
        String s = o.toString().replace("\"", "\"\"");
        // Only wrap in quotes if it contains pipe or newline
        if (s.contains("|") || s.contains("\n")) {
            return "\"" + s + "\"";
        }
        return s;
    }

    private int estimateRowSize(SmsaDownloadResponsePojo h) {
        String raw = String.join("",
                pipe(1),
                pipe(h.getInpOut()),
                pipe(h.getSenderBic()),
                pipe(h.getReceiverBic()),
                pipe(h.getMsgType()),
                pipe(h.getTransactionRef()),
                pipe(h.getTransactionRelatedRefNo()),
                pipe(h.getFileDate()),
                pipe(h.getFileTime()),
                pipe(" "),
                pipe(h.getCurrency()),
                pipe(h.getTransactionAmount()),
                pipe(h.getmText()),
                pipe(" "),
                pipe(h.getFileType()),
                pipe(h.getFileDate()+","+h.getFileTime()),
                pipe(" "),
                pipe(h.getMiorRef())
        );
        return raw.getBytes(StandardCharsets.UTF_8).length;
    }
}
