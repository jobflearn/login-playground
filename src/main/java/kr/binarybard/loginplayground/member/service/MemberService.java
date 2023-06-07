package kr.binarybard.loginplayground.member.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
	private final PasswordEncoder passwordEncoder;

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

			return memberRepository.save(newMember).getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateMemberException(member.getEmail());
		}
	}
}
