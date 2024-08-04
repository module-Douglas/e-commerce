package io.github.douglas.ms_notification.service.impl;


import io.github.douglas.ms_notification.dto.EmailRequestDTO;
import io.github.douglas.ms_notification.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String mailSender;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(EmailRequestDTO request) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSender);
        mailMessage.setSubject(request.subject());
        mailMessage.setText(request.message());
        mailMessage.setTo(request.remitter());

        javaMailSender.send(mailMessage);
    }
}
