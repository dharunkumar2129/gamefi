package com.practivce.auth.service;

public interface EmailService {
	void sendEmail(String[] recipients, String string, String string2);

	void sendVerificationEmail(String email, String randomCode);
}
