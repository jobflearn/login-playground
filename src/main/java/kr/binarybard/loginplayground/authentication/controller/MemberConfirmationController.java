package kr.binarybard.loginplayground.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.binarybard.loginplayground.authentication.domain.ConfirmationToken;
import kr.binarybard.loginplayground.authentication.repository.ConfirmationTokenRepository;
import kr.binarybard.loginplayground.config.exception.ConfirmationTokenExpiredException;
import kr.binarybard.loginplayground.member.repository.MemberRepository;
import kr.binarybard.loginplayground.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberConfirmationController {
	private final MemberService memberService;

	@GetMapping("/confirm")
	public String confirm(String token) {
		memberService.confirmMember(token);
		return "confirmed";
	}

	private void validateToken(ConfirmationToken token) {
		if (token.isExpired()) {
			throw new ConfirmationTokenExpiredException();
		}
	}
}
