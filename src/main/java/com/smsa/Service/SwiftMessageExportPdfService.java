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
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportPdfService {

    @Autowired
    private SmsaDownloadService swiftMessageService;
    private static final Logger logger = LogManager.getLogger(SwiftMessageExportPdfService.class);

    public File exportSelectedMessagesToPdf(String tempDirPath, SwiftMessageHeaderFilterPojo filters) {
        logger.info("Exporting selected messages to PDF. Temp directory: {}, Filters: {}", tempDirPath, filters);

        List<SmsaDownloadResponsePojo> records = swiftMessageService.filterDownloadData(filters);
        if (records == null || records.isEmpty()) {
            logger.warn("No records found to export to PDF.");
            return null;
        }

        File pdfFile = new File(tempDirPath, "swift_messages_" + System.currentTimeMillis() + ".pdf");

        try (PDDocument document = new PDDocument()) {
            PDType1Font font = PDType1Font.HELVETICA;
            float fontSize = 10;
            float leading = 14.5f;
            float margin = 50;
            float yStart = PDRectangle.A4.getHeight() - margin;
            float yPosition = yStart;

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);
            content.setFont(font, fontSize);
            content.setLeading(leading);
            content.beginText();
            content.newLineAtOffset(margin, yPosition);

            for (SmsaDownloadResponsePojo record : records) {
                String[] lines = formatRecord(record).split("\n");

                for (String line : lines) {
                    if (yPosition <= margin + leading) {
                        content.endText();
                        content.close();

                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        content.setFont(font, fontSize);
                        content.setLeading(leading);
                        yPosition = yStart;
                        content.beginText();
                        content.newLineAtOffset(margin, yPosition);
                    }

                    content.showText(line);
                    content.newLine();
                    yPosition -= leading;
                }

                // Extra space between records
                content.newLine();
                yPosition -= leading;
            }

            content.endText();
            content.close();
            document.save(pdfFile);

            logger.info("PDF file successfully created at: {}", pdfFile.getAbsolutePath());
            return pdfFile;

        } catch (IOException e) {
            logger.error("Error while generating PDF file", e);
        } catch (Exception e) {
            logger.error("Unexpected error while exporting messages to PDF", e);
        }

        return null;
    }

    private String formatRecord(SmsaDownloadResponsePojo h) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String dateStr = (h.getFileDate() != null) ? h.getFileDate().format(dateFormatter) : "";

        StringBuilder sb = new StringBuilder();
         sb.append("------------------------------------\n");
        sb.append("Identifier :- ").append(safe(h.getInpOut())).append("\n");
        sb.append("Message Type :- ").append(safe(h.getMsgType())).append("\n");
        sb.append("Sender :- ").append(safe(h.getSenderBic())).append("\n");
        sb.append("Receiver  :- ").append(safe(h.getReceiverBic())).append("\n");
        sb.append("Send\\Receive Date :- ").append(safe(h.getFileDate())).append("\n");
        sb.append("Send\\Receive Time :- ").append(safe(h.getFileTime())).append("\n");
        sb.append("File Type :- ").append(safe(h.getFileType())).append("\n");
        sb.append("Text :- \n");
        sb.append(h.getmText());
        sb.append("\n\n\n");
        return sb.toString();
    }

    private boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
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
        int endIndex = raw.indexOf(endMarker, startIndex);

        if (startIndex == -1 || endIndex == -1) {
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
