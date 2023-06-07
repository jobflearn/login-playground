package kr.binarybard.loginplayground.authentication.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	@Async
	public void sendConfirmationEmail(String to, String token) {
		String confirmationUrl = "http://localhost:8080/confirm?token=" + token;
		String subject = "회원가입 인증 메일입니다.";
		String body = "다음 링크를 클릭해주세요. " + confirmationUrl;

		var message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		mailSender.send(message);
	}
}
