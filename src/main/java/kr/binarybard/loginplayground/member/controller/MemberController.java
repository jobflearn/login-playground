package kr.binarybard.loginplayground.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import kr.binarybard.loginplayground.config.exception.DuplicateMemberException;
import kr.binarybard.loginplayground.member.dto.SignUpRequest;
import kr.binarybard.loginplayground.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

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
	public String signUp(@Valid @ModelAttribute("member") SignUpRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.info("bindingResult has errors: {}", bindingResult);
			return "sign-up";
		}

		try {
			memberService.save(request);
		} catch (DuplicateMemberException e) {
			log.trace("Failed to create account", e);
			bindingResult.reject("exists.email");
			return "sign-up";
		}
		return "redirect:/members/login";
	}
}
