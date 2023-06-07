package kr.binarybard.loginplayground.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.binarybard.loginplayground.config.validator.PasswordConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignUpRequest {
	@NotBlank
	@Email
	private String email;

	@PasswordConstraint
	private String password;
}
