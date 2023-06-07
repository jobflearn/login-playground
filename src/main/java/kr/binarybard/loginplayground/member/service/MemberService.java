package kr.binarybard.loginplayground.member.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var member = memberRepository.findByEmailOrThrow(username);
		return User.builder()
			.username(member.getEmail())
			.password(member.getPassword())
			.build();
	}

	public Long save(SignUpRequest member) {
		var newMember = new Member(member.getEmail(), member.getPassword());
		newMember.encodePassword(passwordEncoder);
		return memberRepository.save(newMember).getId();
	}
}
