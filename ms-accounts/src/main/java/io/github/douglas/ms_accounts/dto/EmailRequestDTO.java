package io.github.douglas.ms_accounts.dto;

public record EmailRequestDTO(
        String subject,
        String remitter,
        String message
) {
}
