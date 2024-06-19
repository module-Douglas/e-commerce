package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class History {

    private Sources source;
    private Status status;
    private String message;
    private LocalDateTime createdAt;
}
