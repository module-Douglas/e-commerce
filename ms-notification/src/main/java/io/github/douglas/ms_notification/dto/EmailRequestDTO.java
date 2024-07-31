package io.github.douglas.ms_notification.dto;

public record EmailRequestDTO(
        String remitter,
        String message
) {
}
