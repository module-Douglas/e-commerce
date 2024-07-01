package io.github.douglas.ms_accounts.service;

public interface ValidationService {
    void validateAccountDetails(String payload);

    void realizeRollback(String payload);
}
