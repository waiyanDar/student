package com.example.student.register.generator;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class OtpGenerator {

	public String generateOtp() {
		String otp = "";
		
		try {
			SecureRandom random = SecureRandom.getInstanceStrong();
			int c = random.nextInt(900000) + 100000;
			otp += c;
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
		
		return otp;
	}
}
