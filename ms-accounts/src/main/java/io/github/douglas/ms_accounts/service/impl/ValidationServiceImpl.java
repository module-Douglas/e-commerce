package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.broker.KafkaProducer;
import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.event.Event;
import io.github.douglas.ms_accounts.dto.event.History;
import io.github.douglas.ms_accounts.enums.Status;
import io.github.douglas.ms_accounts.model.entity.Validation;
import io.github.douglas.ms_accounts.model.repository.UserRepository;
import io.github.douglas.ms_accounts.model.repository.ValidationRepository;
import io.github.douglas.ms_accounts.service.ValidationService;
import io.github.douglas.ms_accounts.utils.JsonUtil;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.github.douglas.ms_accounts.enums.Sources.MS_ACCOUNTS;
import static io.github.douglas.ms_accounts.enums.Status.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ValidationServiceImpl implements ValidationService {

    private final ValidationRepository validationRepository;
    private final UserRepository userRepository;
    private final JsonUtil jsonUtil;
    private final KafkaProducer kafkaProducer;

    public ValidationServiceImpl(
            ValidationRepository validationRepository,
            UserRepository userRepository,
            JsonUtil jsonUtil,
            KafkaProducer kafkaProducer) {
        this.validationRepository = validationRepository;
        this.userRepository = userRepository;
        this.jsonUtil = jsonUtil;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void validateAccountDetails(String payload) {
        var event = jsonUtil.toEvent(payload);
        try {
            var validatedEvent = startValidation(event);
            registerValidation(validatedEvent, true);
            handleSuccess(validatedEvent);
        } catch (Exception e) {
            handleFail(event, e.getMessage());
        }
    }

    @Override
    public void realizeRollback(String payload) {
        var event = jsonUtil.toEvent(payload);
        changeValidationToFail(event);
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, "Rollback executed on MS_ACCOUNTS", FAIL))
        );
    }

    private void handleSuccess(Event event) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, "Account details and Address details successfully validated and retrieved", SUCCESS))
        );
    }

    private void handleFail(Event event, String message) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, message, ROLLBACK_PENDING))
        );
    }

    private Event startValidation(Event event) {
        isPresent(event);
        if (validationRepository.existsByOrderIdAndTransactionId(
                event.id(), event.transactionId()
        )) throw new ValidationException("There is another transactionId for this validation");

        return validateUserAndAddress(event);
    }

    private void isPresent(Event event) {
        if (isEmpty(event.accountDetails().userId())) throw new ValidationException("User id must be informed");
        if (isEmpty(event.deliveryAddress().addressId())) throw new ValidationException("Delivery Address must be informed");
        if (isEmpty(event.id()) || isEmpty(event.transactionId())) throw new ValidationException("OrderId and TransactionId must be informed");
    }

    private Event validateUserAndAddress(Event event) {
        var user = userRepository.findById(UUID.fromString(event.accountDetails().userId()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User not found with id: %s", event.accountDetails().userId())));

        var validatedAddress = user.getAddresses().stream()
                .filter(address -> address.getId().equals(UUID.fromString(event.deliveryAddress().addressId())))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Address with id: %s not found for this user", event.deliveryAddress().addressId())));

        return event.setAccountDetails(validatedAddress, user);
    }


    private void registerValidation(Event event, Boolean status) {
        var validation = new Validation();
        validation.setSuccess(status);
        validation.setOrderId(event.id());
        validation.setTransactionId(event.transactionId());

        validationRepository.save(validation);
    }

    private void changeValidationToFail(Event event) {
        validationRepository
                .findByOrderIdAndTransactionId(event.id(), event.transactionId())
                .ifPresentOrElse(validation -> {
                    validation.setSuccess(false);
                    validationRepository.save(validation);
                }, () -> registerValidation(event, false));

    }

    private Event addHistory(Event event, String message, Status status) {
        var history = new History(MS_ACCOUNTS, status, message, LocalDateTime.now());
        return event.addHistory(history, MS_ACCOUNTS, status);
    }
}
