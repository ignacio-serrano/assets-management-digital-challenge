package com.gft.isz.amdc;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gft.isz.amdc.model.Error;

/* This class could use some logging. */
@ControllerAdvice
public class ErrorAdvice {
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error handle(ConstraintViolationException e) {
		List<String> errorMessages = new ArrayList<>();
		
		for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
			errorMessages.add(cv.getPropertyPath().toString() + " " + cv.getMessage() + ". Was " + cv.getInvalidValue());
		}

		return new Error(errorMessages);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error handle(MethodArgumentNotValidException e) {
		List<String> errorMessages = new ArrayList<>();
		
		for (FieldError fe : e.getBindingResult().getFieldErrors()) {
			errorMessages.add(fe.getObjectName() + "." + fe.getField() + " " + fe.getDefaultMessage());
		}

		return new Error(errorMessages);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Error handle(Throwable t) {
		List<String> errorMessages = new ArrayList<>();
		errorMessages.add("Unknown server error");
		return new Error(errorMessages);
	}

}
