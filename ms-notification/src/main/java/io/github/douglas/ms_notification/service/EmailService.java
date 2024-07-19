package io.github.douglas.ms_notification.service;

import io.github.douglas.ms_notification.dto.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest request);
}
