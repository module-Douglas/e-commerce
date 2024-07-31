package io.github.douglas.ms_notification.service;

import io.github.douglas.ms_notification.dto.EmailRequestDTO;

public interface EmailService {
    void sendEmail(EmailRequestDTO request);
}
