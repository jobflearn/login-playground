package kr.binarybard.loginplayground.config.exception;

public class ConfirmationTokenNotFoundException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = "토큰이 존재하지 않습니다.";

	public ConfirmationTokenNotFoundException() {
		super(DEFAULT_MESSAGE);
	}
}
