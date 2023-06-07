package kr.binarybard.loginplayground.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.binarybard.loginplayground.config.exception.MemberNotFoundException;
import kr.binarybard.loginplayground.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	default Member findByEmailOrThrow(String email) {
		return findByEmail(email)
				.orElseThrow(MemberNotFoundException::new);
	}
}
