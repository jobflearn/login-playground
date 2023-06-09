package kr.binarybard.loginplayground.config.exception;

public class VerificationCodeNotFoundException extends RuntimeException {

    private static final String MESSAGE = "인증 코드를 찾을 수 없습니다. 이메일: %s, 코드: %s";

    public VerificationCodeNotFoundException(String email, String code) {
        super(String.format(MESSAGE, email, code));
    }
}
