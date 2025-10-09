package com.smsa.Service;

import org.apache.logging.log4j.LogManager;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SmsaDownloadServiceImpl.class);

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendMail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ctg@icicibank.com"); // sender (can also take from properties)
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            log.info("Attempting to send mail to: {}, subject: {}", to, subject);

            mailSender.send(message);

            log.info("Mail sent successfully to {}", to);
            return true;

        } catch (Exception e) {
            log.error("Failed to send mail to {} with subject {}. Error: {}", to, subject, e.getMessage(), e);
            return false;
        }
    }
}
