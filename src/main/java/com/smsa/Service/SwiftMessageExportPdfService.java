/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smsa.Service;


/**
 *
 * @author abcom
 */

import com.smsa.entity.SwiftMessageHeader;
import com.smsa.repository.SwiftMessageHeaderRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwiftMessageExportPdfService {

    @Autowired
    private SwiftMessageHeaderRepository repository;

    public File exportSelectedMessagesToPdf(List<String> txnRefs, String tempDirPath) throws IOException {
    List<SwiftMessageHeader> records = repository.findByTransactionRefIn(txnRefs);
    if (records == null || records.isEmpty()) return null;

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

        for (SwiftMessageHeader record : records) {
            String[] lines = formatRecord(record).split("\n");

            for (String line : lines) {
                if (yPosition <= margin + leading) {
                    // Close current content stream and start a new page
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
    }

    return pdfFile;
}


    private String formatRecord(SwiftMessageHeader h) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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

//        if (notBlank(h.()))
//            sb.append(h.getInstanceRaw().trim()).append("\n");
//
//        if (notBlank(h.getHeaderRaw()))
//            sb.append(h.getHeaderRaw().trim()).append("\n");
//
//        sb.append("-----------------Message Text -------------------\n");
//
//        if (notBlank(h.getRawMessageData())) {
//            String cleaned = extractMessageTextSection(h.getRawMessageData());
//            if (notBlank(cleaned))
//                sb.append(cleaned).append("\n");
//        }
//
//        sb.append("------------------------------------\n");
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

        if (startIndex == -1 || endIndex == -1) return "";

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


