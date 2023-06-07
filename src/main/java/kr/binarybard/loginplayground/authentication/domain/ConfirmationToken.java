package kr.binarybard.loginplayground.authentication.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.binarybard.loginplayground.config.exception.ConfirmationTokenExpiredException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "confirmation_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmationToken {
	@Id
	private String token;
	private String email;
	private LocalDateTime createdDate;
	private LocalDateTime expiryDate;

	public ConfirmationToken(String email) {
		this.email = email;
		this.token = UUID.randomUUID().toString();
		this.createdDate = LocalDateTime.now();
		this.expiryDate = this.createdDate.plusHours(24);
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(this.expiryDate);
	}

	public void validate() {
		if (isExpired()) {
			throw new ConfirmationTokenExpiredException();
		}
	}
}
