package kr.binarybard.loginplayground.validator;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
	private static final Pattern PASSWORD_PATTERN =
		Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

	@Override
	public boolean isValid(String passwordField, ConstraintValidatorContext context) {
		return PASSWORD_PATTERN.matcher(passwordField).matches();
	}
}
