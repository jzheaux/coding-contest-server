package edu.neumont.submission.controller;

import javax.persistence.NoResultException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoResultExceptionAdvice {
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoResultException.class)
	public void handleNotFound() {}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NullPointerException.class)
	public void handleNotFoundDueToNull() {}
}
