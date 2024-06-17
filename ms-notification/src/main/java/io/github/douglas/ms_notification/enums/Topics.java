package io.github.douglas.ms_notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {

    SEND_EMAIL("send-email");

    private final String topic;
}
