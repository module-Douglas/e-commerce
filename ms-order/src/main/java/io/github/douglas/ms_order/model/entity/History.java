package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;

import java.time.LocalDateTime;


public class History {

    private Sources source;
    private Status status;
    private String message;
    private LocalDateTime createdAt;

    public History() {
    }

    public History(Sources source, Status status, String message, LocalDateTime createdAt) {
        this.source = source;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
    }

    public Sources getSource() {
        return source;
    }

    public void setSource(Sources source) {
        this.source = source;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
