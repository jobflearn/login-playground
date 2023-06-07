package kr.binarybard.loginplayground.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain formFilterChain(final HttpSecurity http) throws Exception {
		return http
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(toH2Console())
				.disable())
			.authorizeHttpRequests(
				auth -> auth
					.requestMatchers(toH2Console()).permitAll()
					.requestMatchers("/members/login", "/members/signup", "/confirm").permitAll()
					.anyRequest().authenticated()
			)
			.headers(headers -> headers
				.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.formLogin(login -> login
				.loginPage("/members/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/", true))
			.logout(logout -> logout
				.logoutSuccessUrl("/"))
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
			.build();
	}
}
