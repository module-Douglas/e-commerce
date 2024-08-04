package io.github.douglas.ms_notification.dto;

public record EmailRequestDTO(
        String subject,
        String remitter,
        String message
) {
}
