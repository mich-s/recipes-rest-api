package com.michal.recipes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RecipeExceptionHandler {

	
	@ExceptionHandler
	public ResponseEntity<RecipeErrorResponse> handleException(RecipeNotFoundException exception) {
		RecipeErrorResponse errorResponse = new RecipeErrorResponse();
		
		errorResponse.setMessage(exception.getMessage());
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<RecipeErrorResponse> handleException(Exception exception) {
		RecipeErrorResponse errorResponse = new RecipeErrorResponse();
		
		errorResponse.setMessage(exception.getMessage());
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setTimestamp(System.currentTimeMillis());
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	
	
	
	
	
}

