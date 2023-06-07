package kr.binarybard.loginplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class LoginPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginPlaygroundApplication.class, args);
	}

}
