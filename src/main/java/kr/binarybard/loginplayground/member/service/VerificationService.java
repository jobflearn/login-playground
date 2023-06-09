package kr.binarybard.loginplayground.member.service;

import java.time.LocalDateTime;
import java.util.Random;
import kr.binarybard.loginplayground.config.exception.VerificationCodeNotFoundException;
import kr.binarybard.loginplayground.mail.service.MailService;
import kr.binarybard.loginplayground.member.domain.VerificationCode;
import kr.binarybard.loginplayground.member.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final MailService mailService;

    public VerificationCode createVerificationCode(String email) {
        String code = generateVerificationCode();
        var verificationCode = VerificationCode.builder()
            .email(email)
            .code(code)
            .creationDateTime(LocalDateTime.now())
            .expirationDateTime(LocalDateTime.now().plusHours(24))
            .build();
        return verificationCodeRepository.save(verificationCode);
    }

    public boolean verifyCode(String email, String code) {
        try {
            var verificationCode = verificationCodeRepository.findByEmailAndCodeOrThrow(email,
                code);
            if (verificationCode != null && verificationCode.getExpirationDateTime()
                .isAfter(LocalDateTime.now())) {
                verificationCodeRepository.delete(verificationCode);
                return true;
            }
        } catch (VerificationCodeNotFoundException e) {
            log.info("Verification code not found");
        }
        return false;
    }

    private String generateVerificationCode() {
        int codeLength = 6;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public String sendVerificationCode(String email) {
        VerificationCode verificationCode = createVerificationCode(email);
        String code = verificationCode.getCode();
        String formattedCode = code.substring(0, 3) + " - " + code.substring(3);

        String htmlContent = """
    <html>
        <body style='font-family: Arial, sans-serif;'>
            <div style='max-width: 600px; margin: 0 auto; background-color: #f8f9fa; padding: 20px; border-radius: 10px; border: 1px solid #848484; box-shadow: 0 0 10px rgba(0,0,0,0.15);'>
                <h1 style='color: #3498db; text-align: center;'>서비스에 오신 것을 환영합니다!</h1>
                <div style='border: 1px solid #404040; padding: 20px; margin: 20px 0; border-radius: 10px;'>
                    <p style='font-size: 16px; line-height: 1.5;'>서비스에 참여해주셔서 감사합니다. 시작하기 전에, 이메일 주소를 인증해야 합니다.</p>
                    <h2 style='text-align: center; color: #404040;'>인증 코드:</h2>
                    <div style='text-align: center; justify-content: center; align-items: center;'>
                        <h3 style='color: #e74c3c; padding: 10px 20px; border: 2px solid #e74c3c; display: inline-block; border-radius: 5px; font-size: 2em; background-color: #ecf0f1;'>%s</h3>
                    </div>
                </div>
                <p style='font-size: 16px; line-height: 1.5;'>회원가입 신청을 하지 않으셨다면, 이 이메일은 무시하셔도 됩니다.</p>
                <p style='text-align: center;'>감사합니다, <br/> 팀 드림</p>
            </div>
        </body>
    </html>
    """.formatted(formattedCode);

        mailService.sendHtmlMail(email, "Verification Code", htmlContent);
        return verificationCode.getCode();
    }





}
