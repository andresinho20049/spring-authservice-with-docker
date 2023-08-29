package com.andresinho20049.authservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidateService {
	
	public boolean matcher(String value, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		
		return matcher.matches();
	}

}
