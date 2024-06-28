package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.model.repository.ValidationRepository;
import io.github.douglas.ms_accounts.service.ValidationService;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final ValidationRepository validationRepository;

    public ValidationServiceImpl(ValidationRepository validationRepository) {
        this.validationRepository = validationRepository;
    }

}
