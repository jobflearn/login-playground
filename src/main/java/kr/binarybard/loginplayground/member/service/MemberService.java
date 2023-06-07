package kr.binarybard.loginplayground.member.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.binarybard.loginplayground.authentication.domain.ConfirmationToken;
import kr.binarybard.loginplayground.authentication.repository.ConfirmationTokenRepository;
import kr.binarybard.loginplayground.authentication.service.EmailService;
import kr.binarybard.loginplayground.config.exception.ConfirmationTokenExpiredException;
import kr.binarybard.loginplayground.config.exception.DuplicateMemberException;
import kr.binarybard.loginplayground.config.exception.MemberNotFoundException;
import kr.binarybard.loginplayground.member.domain.Member;
import kr.binarybard.loginplayground.member.dto.SignUpRequest;
import kr.binarybard.loginplayground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
	private final MemberRepository memberRepository;
	private final ConfirmationTokenRepository confirmationTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Override
	public UserDetails loadUserByUsername(String username) throws MemberNotFoundException {
		var member = memberRepository.findByEmailOrThrow(username);
		return User.builder()
			.username(member.getEmail())
			.password(member.getPassword())
			.build();
	}

	@Transactional
	public Long save(SignUpRequest member) {
		try {
			var newMember = new Member(member.getEmail(), member.getPassword());
			newMember.encodePassword(passwordEncoder);

			Long id = memberRepository.save(newMember).getId();
			var token = saveConfirmationToken(member.getEmail());
			emailService.sendConfirmationEmail(member.getEmail(), token);

			return id;
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateMemberException(member.getEmail());
		}
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
