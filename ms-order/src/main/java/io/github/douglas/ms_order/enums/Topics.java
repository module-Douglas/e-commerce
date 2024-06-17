package io.github.douglas.ms_order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Topics {

    START_SAGA("start-saga"),
    NOTIFY_ENDING("notify-ending");

    private final String topic;
}
