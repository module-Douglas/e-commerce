package io.github.douglas.ms_order.enums;


public enum Topics {

    START_SAGA("start-saga"),
    NOTIFY_ENDING("notify-ending"),
    SEND_EMAIL("send-email");

    private final String topic;

    Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
