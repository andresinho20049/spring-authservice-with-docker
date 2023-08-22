package com.andresinho20049.authservice.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.andresinho20049.authservice.dto.StandardResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler({ProjectException.class, Exception.class})
	public ResponseEntity<StandardResponse> responseError(Throwable throwable, HttpServletRequest request){
		
		StringBuilder builder = new StringBuilder();
		log.error("###############################");
		while (throwable != null) {
			builder.append(throwable.getMessage()  + ". ");
			log.error("getMessage: " + throwable.getMessage() + "-----------------");
			for(StackTraceElement s : throwable.getStackTrace()) {
				log.error(s.getClassName() + " - method: " + s.getMethodName() + " - line: " + s.getLineNumber());
			}
			throwable = throwable.getCause();
		}
		String msg = builder.toString();
		
		StandardResponse body = new StandardResponse(HttpServletResponse.SC_BAD_REQUEST, msg, System.currentTimeMillis());
		return ResponseEntity.badRequest().body(body);
	}
	
	@ExceptionHandler({AuthorizationException.class, AccessDeniedException.class})
    public ResponseEntity<StandardResponse> handleAuthenticationException(Exception e, HttpServletRequest request) {
		
		StandardResponse body = new StandardResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage(), System.currentTimeMillis());
		return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(body);
    }
}
