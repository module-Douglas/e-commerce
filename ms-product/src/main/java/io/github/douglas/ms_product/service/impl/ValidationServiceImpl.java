package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.broker.KafkaProducer;
import io.github.douglas.ms_product.config.exception.ValidationException;
import io.github.douglas.ms_product.dto.event.Event;
import io.github.douglas.ms_product.dto.event.History;
import io.github.douglas.ms_product.enums.Status;
import io.github.douglas.ms_product.model.entity.Validation;
import io.github.douglas.ms_product.model.repository.ProductRepository;
import io.github.douglas.ms_product.model.repository.ValidationRepository;
import io.github.douglas.ms_product.service.ValidationService;
import io.github.douglas.ms_product.utils.JsonUtil;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.github.douglas.ms_product.enums.Sources.MS_PRODUCT;
import static io.github.douglas.ms_product.enums.Status.*;
import static org.springframework.util.ObjectUtils.isEmpty;


@Service
public class ValidationServiceImpl implements ValidationService {

    private static final Logger log = LoggerFactory.getLogger(ValidationServiceImpl.class);
    private final ProductRepository productRepository;
    private final ValidationRepository validationRepository;
    private final JsonUtil jsonUtil;
    private final KafkaProducer kafkaProducer;

    public ValidationServiceImpl(
            ProductRepository productRepository,
            ValidationRepository validationRepository,
            JsonUtil jsonUtil,
            KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.validationRepository = validationRepository;
        this.jsonUtil = jsonUtil;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void validateProducts(String payload) {
        var event = jsonUtil.toEvent(payload);
        try {
            startValidation(event);
            registerValidation(event, true);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Error trying to validate products: ", e);
            handleFail(event, e.getMessage());
        }
    }

    @Override
    public void realizeRollback(String payload) {
        var event = jsonUtil.toEvent(payload);
        changeValidationToFail(event);
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, "Rollback executed on MS_PRODUCT", FAIL))
        );
    }

    private void handleSuccess(Event event) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, "Products successfully validated", SUCCESS))
        );
    }

    private void handleFail(Event event, String message) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, message, ROLLBACK_PENDING))
        );
    }

    private void startValidation(Event event) {
        isPresent(event);
        if (validationRepository.existsByOrderIdAndTransactionId(
                event.id(), event.transactionId()
        )) throw new ValidationException("There is another transactionId for this validation.");

        event.products().forEach(
                product -> validateProductInformed(product.productId())
        );
    }

    private void isPresent(Event event) {
        if (isEmpty(event.products())) throw new ValidationException("Product list is empty");
        if (isEmpty(event.id()) || isEmpty(event.transactionId())) throw new ValidationException("OrderId and TransactionID must be informed.");
    }

    private void validateProductInformed(UUID productId) {
        if (!productRepository.existsById(productId))
            throw new ResourceNotFoundException(String.format("Product not found with id: %s", productId));
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
        var history = new History(MS_PRODUCT, status, message, LocalDateTime.now());
        return event.addHistory(history, MS_PRODUCT, status);
    }


}
