package com.practivce.auth.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.practivce.auth.service.EmailService;

@Component
public class EmailSchedular {

	@Autowired
	private EmailService emailService; // Directly inject the class

	@Scheduled(cron = "0 0/1 * * * ?") // every 10 minutes
	public void sendScheduledEmail() {

		// List of recipients
		String[] recipients = { "ytubetamil7@gmail.com"};

		emailService.sendEmail(recipients, "Scheduled Email from Spring Boot",
				"Hello! This is a scheduled email sent automatically every 10 minutes.");
	}
}
