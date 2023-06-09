package kr.binarybard.loginplayground.mail.service;

public interface MailService {
    void sendMail(String to, String subject, String text);
    void sendHtmlMail(String to, String subject, String text);
}
