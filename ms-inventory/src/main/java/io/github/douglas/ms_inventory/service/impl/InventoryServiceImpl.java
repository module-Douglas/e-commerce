package io.github.douglas.ms_inventory.service.impl;

import io.github.douglas.ms_inventory.broker.KafkaProducer;
import io.github.douglas.ms_inventory.config.exception.ValidationException;
import io.github.douglas.ms_inventory.dto.InventoryDTO;
import io.github.douglas.ms_inventory.dto.LinkInventory;
import io.github.douglas.ms_inventory.dto.event.Event;
import io.github.douglas.ms_inventory.dto.event.History;
import io.github.douglas.ms_inventory.dto.event.Product;
import io.github.douglas.ms_inventory.enums.Status;
import io.github.douglas.ms_inventory.model.entity.Inventory;
import io.github.douglas.ms_inventory.model.entity.InventoryHistoric;
import io.github.douglas.ms_inventory.model.repository.InventoryHistoricRepository;
import io.github.douglas.ms_inventory.model.repository.InventoryRepository;
import io.github.douglas.ms_inventory.service.InventoryService;
import io.github.douglas.ms_inventory.utils.JsonUtil;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.github.douglas.ms_inventory.enums.Sources.MS_INVENTORY;
import static io.github.douglas.ms_inventory.enums.Status.*;
import static java.lang.String.format;

@Service
public class InventoryServiceImpl implements InventoryService {


    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepository inventoryRepository;
    private final JsonUtil jsonUtil;
    private final InventoryHistoricRepository inventoryHistoricRepository;
    private final KafkaProducer kafkaProducer;

    public InventoryServiceImpl(
            InventoryRepository inventoryRepository,
            JsonUtil jsonUtil,
            InventoryHistoricRepository inventoryHistoricRepository,
            KafkaProducer kafkaProducer) {
        this.inventoryRepository = inventoryRepository;
        this.jsonUtil = jsonUtil;
        this.inventoryHistoricRepository = inventoryHistoricRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void registerInventory(String payload) {
        var inventory = inventoryRepository.save(
                jsonUtil.toInventory(payload)
        );
        kafkaProducer.sendInventoryLink(
                jsonUtil.toJson(new LinkInventory(inventory.getProductId(), inventory.getId()))
        );
    }

    @Override
    public InventoryDTO findByProductId(UUID productId) {
        return new InventoryDTO(
                inventoryRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );
    }

    @Override
    public void orderUpdate(String payload) {
        var event = jsonUtil.toEvent(payload);
        try {
            startValidation(event);
            registerInventoryHistoric(event);
            updateInventory(event);
            handleSuccess(event);
        } catch (Exception e) {
            handleFail(event, e.getMessage());
        }
    }

    @Override
    public void rollbackInventory(String payload) {
        var event = jsonUtil.toEvent(payload);
        try {
            returnInventoryToPrevious(event);
            kafkaProducer.sendEvent(
                    jsonUtil.toJson(addHistory(event, "Rollback successfully executed.", FAIL))
            );
        } catch (Exception e) {
            kafkaProducer.sendEvent(
                    jsonUtil.toJson(addHistory(event, format("Rollback couldn't be executed: %s", e.getMessage()), FAIL))
            );
        }
    }

    @Override
    public InventoryDTO findById(UUID id) {
        return new InventoryDTO(
                inventoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(format("Inventory not found with id: %s", id)))
        );
    }

    @Override
    public void updateInventory(String payload) {
        var data = jsonUtil.toUpdateInventory(payload);
        var inventory = inventoryRepository.findByProductId(data.productId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Inventory not found for product id: %s", data.productId())));

        inventory.setUnitValue(data.unitValue());
        inventoryRepository.save(inventory);
    }

    private void handleSuccess(Event event) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, "Product disposability successfully validated.", SUCCESS))
        );
    }

    private void handleFail(Event event, String message) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, message, ROLLBACK_PENDING))
        );
    }

    private void startValidation(Event event) {
        if (inventoryHistoricRepository.existsByOrderIdAndTransactionId(
                event.id(), event.transactionId()
        )) throw new ValidationException(format("There is another transactionId for this validation: %s", event.id()));
    }

    private void registerInventoryHistoric(Event event) {
        event.products()
                .forEach(product -> {
                     var inventory = getInventoryByProductId(product.productId());
                     var inventoryHistoric = registerInventoryHistoric(event, product, inventory);
                    inventoryHistoricRepository.save(inventoryHistoric);
                });
    }

    private void updateInventory(Event event) {
        event.products()
                .forEach(product -> {
                    var inventory = getInventoryByProductId(product.productId());
                    checkInventory(inventory.getStockAmount(), product.quantity());
                    inventory.setStockAmount(inventory.getStockAmount() - product.quantity());
                    inventoryRepository.save(inventory);
                });
    }

    private void checkInventory(Long stockAmount, Long quantity) {
        if (quantity > stockAmount) throw new ValidationException("Product is out of stock!");
    }

    private InventoryHistoric registerInventoryHistoric(Event event, Product product, Inventory inventory) {
        return new InventoryHistoric(
                inventory,
                event.id(),
                event.transactionId(),
                product.quantity(),
                inventory.getStockAmount(),
                (inventory.getStockAmount() - product.quantity())
        );
    }

    private Inventory getInventoryByProductId(UUID productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Inventory not found for id: %s", productId)));
    }

    private Event addHistory(Event event, String message, Status status) {
        var history = new History(MS_INVENTORY, status, message, LocalDateTime.now());
        return event.addHistory(history, MS_INVENTORY, status);
    }

    private void returnInventoryToPrevious(Event event) {
        inventoryHistoricRepository
                .findByOrderIdAndTransactionId(event.id(), event.transactionId())
                .forEach(inventoryHistoric -> {
                    var inventory = inventoryHistoric.getInventory();
                    inventory.setStockAmount(inventoryHistoric.getOldQuantity());
                    inventoryRepository.save(inventory);
                    log.info("Restored inventory {} for order {} from {} to {}",
                            inventory.getId(), event.id(), inventoryHistoric.getNewQuantity(), inventoryHistoric.getOldQuantity());
                });
    }
}
