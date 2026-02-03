package com.practivce.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dharunkumar2129@gmail.com"); // YOUR EMAIL HERE
        message.setTo(toEmail);
        message.setSubject("GameFi Email Verification");
        message.setText("Your verification code is: " + code);
        
        mailSender.send(message);
    }
}