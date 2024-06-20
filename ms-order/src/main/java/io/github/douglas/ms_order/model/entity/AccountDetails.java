package io.github.douglas.ms_order.model.entity;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {

    private UUID userId;
    private String email;
}
