package kr.binarybard.loginplayground.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.binarybard.loginplayground.authentication.domain.ConfirmationToken;
import kr.binarybard.loginplayground.config.exception.ConfirmationTokenNotFoundException;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
	default ConfirmationToken findByTokenOrThrow(String token) {
		return findById(token)
			.orElseThrow(ConfirmationTokenNotFoundException::new);
	}
}
