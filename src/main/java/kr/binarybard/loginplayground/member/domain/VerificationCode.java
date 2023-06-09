package kr.binarybard.loginplayground.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "verification_codes")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class VerificationCode {
    @Id @GeneratedValue
    @Column(name = "verification_code_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime creationDateTime;

    @Column(nullable = false)
    private LocalDateTime expirationDateTime;

    @Builder
    public VerificationCode(Long id, String email, String code, LocalDateTime creationDateTime,
        LocalDateTime expirationDateTime) {
        this.id = id;
        this.email = email;
        this.code = code;
        this.creationDateTime = creationDateTime;
        this.expirationDateTime = expirationDateTime;
    }
}
