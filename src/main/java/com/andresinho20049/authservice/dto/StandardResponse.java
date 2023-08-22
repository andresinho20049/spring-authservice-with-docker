package com.andresinho20049.authservice.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class StandardResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String msg;
	private String dateTime;
	
	public StandardResponse(Integer status, String msg, Long timeStamp) {
		super();
		this.status = status;
		this.msg = msg;
		this.dateTime = formatTimeStampToDate(timeStamp);
	}
	
	private String formatTimeStampToDate(long timeStamp) {
	       Date currentDate = new Date(timeStamp);
	       DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	       return df.format(currentDate);
	}
	
}
