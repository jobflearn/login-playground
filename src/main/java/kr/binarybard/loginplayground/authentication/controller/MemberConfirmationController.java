package kr.binarybard.loginplayground.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.binarybard.loginplayground.authentication.service.EmailConfirmationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberConfirmationController {
	private final EmailConfirmationService emailConfirmationService;

	@GetMapping("/confirm")
	public String confirm(String token) {
		emailConfirmationService.confirmMember(token);
		return "confirmed";
	}
}
