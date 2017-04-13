package com.gft.isz.amdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@ControllerAdvice
public class ErrorAdvice {
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> handle(ConstraintViolationException e) {
		Map<String, Object> ret = new HashMap<>();
		Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		List<String> errorMessages = new ArrayList<>();
		for (ConstraintViolation<?> cv : constraintViolations) {
			errorMessages.add(cv.getPropertyPath().toString() + " " + cv.getMessage() + ". Was " + cv.getInvalidValue());
		}
		ret.put("errors", errorMessages);
		return ret;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> handle(MethodArgumentNotValidException e) {
		Map<String, Object> ret = new HashMap<>();
		List<String> errorMessages = new ArrayList<>();
		for (FieldError fe : e.getBindingResult().getFieldErrors()) {
			System.out.println("Trial: " + fe.getObjectName() + "." + fe.getField() + " " + fe.getDefaultMessage());
			errorMessages.add(fe.getObjectName() + "." + fe.getField() + " " + fe.getDefaultMessage());
		}
		ret.put("errors", errorMessages);
		return ret;
	}
	
}