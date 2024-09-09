package io.github.douglas.ms_notification.enums;

public enum Topics {

    SEND_EMAIL("send-email");

    private final String topic;

     Topics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
