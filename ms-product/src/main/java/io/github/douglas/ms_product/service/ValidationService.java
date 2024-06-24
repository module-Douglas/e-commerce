package io.github.douglas.ms_product.service;

public interface ValidationService {
    void validateProducts(String payload);

    void realizeRollback(String payload);
}
