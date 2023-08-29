package com.andresinho20049.authservice.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RegexValidateTest {
	
	private RegexValidateService regexValidate;
	
	public RegexValidateTest() {
		super();
		this.regexValidate = new RegexValidateService();
	}

	@Test
	void validatePasswordIsStrong() {
		
		String regexPassword = "^(?=.*\\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		
		String passwordStrong = "UPdown@1234";
		Boolean match = regexValidate.matcher(passwordStrong, regexPassword);
		assertTrue(match);
		
		
		String[] weakPasswords = {"12345678", "abc", "xuxa", "letra123456", "a1b2c3"};
		for (String pass : weakPasswords) {
			assertFalse(regexValidate.matcher(pass, regexPassword));
		}
		
	}
	
	@Test
	void validateEmailIsValid() {

		String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

		String[] emailsValid = {"user@email.com", "pessoa@domain.com", "maria@gmail.com", "test@a5solutions.com"};
		for (String email : emailsValid) {
			assertTrue(regexValidate.matcher(email, regexEmail));
		}

		String[] emailsNotValid = {"user.com", "@domain.com", "maria@com", "maria.test@domain", "test email is not valid"};
		for (String email : emailsNotValid) {
			assertFalse(regexValidate.matcher(email, regexEmail));
		}
		
		
	}

}
