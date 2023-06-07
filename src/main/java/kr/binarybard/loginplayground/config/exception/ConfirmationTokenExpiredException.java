package kr.binarybard.loginplayground.config.exception;

public class ConfirmationTokenExpiredException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = "토큰이 만료되었습니다.";

	public ConfirmationTokenExpiredException() {
		super(DEFAULT_MESSAGE);
	}
}
