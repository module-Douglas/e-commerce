package io.github.douglas.ms_accounts.dto.event;


import io.github.douglas.ms_accounts.enums.Sources;
import io.github.douglas.ms_accounts.enums.Status;

import java.time.LocalDateTime;

public record History(
        Sources source,
        Status status,
        String message,
        LocalDateTime createdAt
) {
}
