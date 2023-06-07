package kr.binarybard.loginplayground.authentication.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.binarybard.loginplayground.authentication.domain.ConfirmationToken;
import kr.binarybard.loginplayground.authentication.repository.ConfirmationTokenRepository;
import kr.binarybard.loginplayground.config.exception.EmailNotSentException;
import kr.binarybard.loginplayground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final EmailService emailService;
	private final MemberRepository memberRepository;

	public void sendConfirmationEmail(String email) {
		var token = saveConfirmationToken(email);

		emailService.sendConfirmationEmail(email, token)
			.exceptionally(e -> {
				throw new EmailNotSentException(e);
			});
	}

	private String saveConfirmationToken(String email) {
		var token = new ConfirmationToken(email);
		confirmationTokenRepository.save(token);
		return token.getToken();
	}

	@Transactional
	public void confirmMember(String token) {
		var confirmationToken = confirmationTokenRepository.findByTokenOrThrow(token);
		confirmationToken.validate();
		var member = memberRepository.findByEmailOrThrow(confirmationToken.getEmail());
		member.confirm();

		confirmationTokenRepository.deleteById(token);
	}
}