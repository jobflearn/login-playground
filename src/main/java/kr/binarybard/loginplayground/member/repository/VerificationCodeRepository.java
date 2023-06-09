package kr.binarybard.loginplayground.member.repository;

import java.util.Optional;
import kr.binarybard.loginplayground.config.exception.VerificationCodeNotFoundException;
import kr.binarybard.loginplayground.member.domain.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmailAndCode(String email, String code);

    default VerificationCode findByEmailAndCodeOrThrow(String email, String code) {
        return findByEmailAndCode(email, code)
            .orElseThrow(() -> new VerificationCodeNotFoundException(email, code));
    }
}
