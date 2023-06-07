package kr.binarybard.loginplayground.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import kr.binarybard.loginplayground.exception.DuplicateMemberException;
import kr.binarybard.loginplayground.member.domain.Member;
import kr.binarybard.loginplayground.member.dto.SignUpRequest;
import kr.binarybard.loginplayground.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberService memberService;

	@Test
	public void testLoadUserByUsername_Success() {
		// given
		String username = "test@binarybard.kr";
		Member member = new Member(username, "password");
		when(memberRepository.findByEmailOrThrow(username)).thenReturn(member);

		// when
		UserDetails userDetails = memberService.loadUserByUsername(username);

		// then
		assertThat(username).isEqualTo(userDetails.getUsername());
	}

	@Test
	public void testLoadUserByUsername_NotFound() {
		// given
		String username = "test@binarybard.kr";
		when(memberRepository.findByEmailOrThrow(username)).thenThrow(new UsernameNotFoundException("User not found"));

		assertThatThrownBy(() -> {
			// when
			memberService.loadUserByUsername(username);
		}).isInstanceOf(UsernameNotFoundException.class);
	}

	@Test
	public void testSave_Success() {
		// given
		String email = "test@binarybard.kr";
		String password = "password";
		SignUpRequest request = new SignUpRequest(email, password);
		Member member = new Member(email, password);
		ReflectionTestUtils.setField(member, "id", 1L);

		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(memberRepository.save(any(Member.class))).thenReturn(member);

		// when
		Long memberId = memberService.save(request);

		// then
		assertThat(memberId).isEqualTo(1L);
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	public void testSave_DuplicateEmail() {
		// given
		String email = "test@binarybard.kr";
		String password = "password";
		SignUpRequest request = new SignUpRequest(email, password);
		when(memberRepository.save(any(Member.class))).thenThrow(new DuplicateMemberException(email));

		// then
		assertThatThrownBy(() -> {
			// when
			memberService.save(request);
		}).isInstanceOf(DuplicateMemberException.class);
	}
}
