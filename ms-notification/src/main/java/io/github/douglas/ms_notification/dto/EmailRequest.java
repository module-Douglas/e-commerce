package io.github.douglas.ms_notification.dto;

public record EmailRequest(
        String email,
        String message,
        String subject
) {
}
