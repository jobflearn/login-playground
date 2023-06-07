package kr.binarybard.loginplayground.config.exception;

public class EmailNotSentException extends RuntimeException {

	private static final String MESSAGE = "이메일 전송에 실패했습니다.";

	public EmailNotSentException() {
		super(MESSAGE);
	}

	public EmailNotSentException(Throwable cause) {
		super(MESSAGE, cause);
	}
}
