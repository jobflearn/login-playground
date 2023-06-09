package kr.binarybard.loginplayground.member.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.binarybard.loginplayground.config.exception.DuplicateMemberException;
import kr.binarybard.loginplayground.member.dto.SignUpRequest;
import kr.binarybard.loginplayground.member.service.MemberService;
import kr.binarybard.loginplayground.member.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final VerificationService verificationService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signUp(Model model) {
		var request = SignUpRequest.builder().build();
		model.addAttribute("member", request);
		return "sign-up";
	}

	@PostMapping("/signup")
	public String signUp(@Valid @ModelAttribute("member") SignUpRequest request, BindingResult bindingResult, HttpSession session) {
		if (bindingResult.hasErrors()) {
			log.info("bindingResult has errors: {}", bindingResult);
			return "sign-up";
		}

		try {
			String code = verificationService.sendVerificationCode(request.getEmail());
			session.setAttribute("member", request);
			session.setAttribute("verificationCode", code);
			return "verify";
		} catch (Exception e) {
			log.error("Failed to send verification code", e);
			bindingResult.reject("email.failedToSendCode");
			return "sign-up";
		}
	}

	@PostMapping("/verify")
	public String verify(@RequestParam("code1") String code1,
		@RequestParam("code2") String code2,
		@RequestParam("code3") String code3,
		@RequestParam("code4") String code4,
		@RequestParam("code5") String code5,
		@RequestParam("code6") String code6,
		HttpSession session,
		Model model) {

		String enteredCode = code1 + code2 + code3 + code4 + code5 + code6;
		SignUpRequest request = (SignUpRequest) session.getAttribute("member");

		String email = request.getEmail();

		if (verificationService.verifyCode(email, enteredCode)) {
			try {
				memberService.save(request);
				session.removeAttribute("member");
				return "login";
			} catch (DuplicateMemberException e) {
				model.addAttribute("error", "이미 가입된 이메일입니다.");
				return "sign-up";
			}
		} else {
			model.addAttribute("error", "인증번호가 일치하지 않습니다.");
			return "verify";
		}
	}
}
