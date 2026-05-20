package com.practivce.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your_email@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Email Verification Code");
            message.setText("Your OTP is: " + code + "\n\nValid for 5 minutes.");

            mailSender.send(message);
            logger.info("Verification email sent successfully to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
}