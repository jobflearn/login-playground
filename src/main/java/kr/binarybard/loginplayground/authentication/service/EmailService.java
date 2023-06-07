package kr.binarybard.loginplayground.authentication.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	@Async
	public CompletableFuture<Void> sendConfirmationEmail(String to, String token) {
		try {
			String confirmationUrl = "http://localhost:8080/confirm?token=" + token;
			String subject = "회원가입 인증 메일입니다.";
			String body = "다음 링크를 클릭해주세요. " + confirmationUrl;

			var message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);

			mailSender.send(message);
			return CompletableFuture.completedFuture(null);
		} catch (MailException e) {
			CompletableFuture<Void> future = new CompletableFuture<>();
			future.completeExceptionally(e);
			return future;
		}
	}
}
