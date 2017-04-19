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

/* This class takes care of producing, when possible, a meaningful output in 
 * case of error. Not returning Java stack traces to the consumer also helps
 * keeping the application safe, since stack traces are clear hints of the 
 * technologies involved in the service (and their unfixed security issues).
 * With more time I would log all errors, and will also cover more types
 * of errors (currently only validation errors are covered). */
@ControllerAdvice
public class ErrorAdvice {

	/* This handles validation errors in complex method parameters such as those
	 * coming from POST request bodies. */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error handle(ConstraintViolationException e) {
		List<String> errorMessages = new ArrayList<>();

		for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
			errorMessages
					.add(cv.getPropertyPath().toString() + " " + cv.getMessage() + ". Was " + cv.getInvalidValue());
		}

		return new Error(errorMessages);
	}

	/* This handles validation errors in simple method parameters such as those
	 * coming from GET requests (request and URI parameters). */
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
