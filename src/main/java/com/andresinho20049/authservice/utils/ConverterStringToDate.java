package com.andresinho20049.authservice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.andresinho20049.authservice.exceptions.ProjectException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConverterStringToDate {
	
	private SimpleDateFormat dateFormat;
	
	public ConverterStringToDate() {
		super();
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}

	public ConverterStringToDate(String format) {
		super();
		
		dateFormat = new SimpleDateFormat(format);
	}

	public String convertToDatabaseColumn(Date attribute) {
		return dateFormat.format(attribute);
	}

	public Date convertToEntityAttribute(String dbData) {
		try {
			return dateFormat.parse(dbData);
		} catch (ParseException e) {
			log.error(e.getMessage());
			throw new ProjectException(dbData, e);
		}
	}

}
