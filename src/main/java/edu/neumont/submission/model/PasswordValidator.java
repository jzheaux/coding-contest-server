package edu.neumont.submission.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, char[]> {

	@Override
	public void initialize(Password arg0) {	
	}

	@Override
	public boolean isValid(char[] arg0, ConstraintValidatorContext arg1) {
		return arg0 != null && arg0.length >= 8; 
	}
}
