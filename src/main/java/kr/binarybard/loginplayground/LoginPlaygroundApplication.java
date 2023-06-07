package kr.binarybard.loginplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LoginPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginPlaygroundApplication.class, args);
	}

}
