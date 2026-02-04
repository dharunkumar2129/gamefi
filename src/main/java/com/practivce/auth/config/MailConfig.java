package com.practivce.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // FORCE PORT 465 (SSL) - This is the "Nuclear Option" for cloud servers
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps"); // Note the 's' at the end
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.starttls.enable", "true");
        props.put("mail.smtps.timeout", "20000"); // 20 seconds
        
        // Debugging: This will show the exact handshake in the logs
        props.put("mail.debug", "true"); 

        return mailSender;
    }
}