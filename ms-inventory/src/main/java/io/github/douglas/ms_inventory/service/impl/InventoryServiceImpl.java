package io.github.douglas.ms_inventory.service.impl;

import io.github.douglas.ms_inventory.broker.KafkaProducer;
import io.github.douglas.ms_inventory.config.exception.ValidationException;
import io.github.douglas.ms_inventory.dto.InventoryDTO;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static io.github.douglas.ms_inventory.enums.Sources.MS_INVENTORY;
import static io.github.douglas.ms_inventory.enums.Status.FAIL;
import static io.github.douglas.ms_inventory.enums.Status.SUCCESS;
import static java.lang.String.format;

@Service
public class InventoryServiceImpl implements InventoryService {

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
        inventoryRepository.save(
                jsonUtil.toInventory(payload)
        );
    }

    @Override
    public InventoryDTO findByProductId(String productId) {
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

    private void handleSuccess(Event event) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, "Product disposability successfully validated.", SUCCESS))
        );
    }

    private void handleFail(Event event, String message) {
        kafkaProducer.sendEvent(
                jsonUtil.toJson(addHistory(event, message, FAIL))
        );
    }

    private void startValidation(Event event) {
        if (inventoryHistoricRepository.existsByOrderIdAndTransactionId(
                event.id(), event.transactionId()
        )) throw new ValidationException("There is another transactionId for this validation.");
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

    private Inventory getInventoryByProductId(String productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException(format("Inventory not found for id: %s", productId)));
    }

    private Event addHistory(Event event, String message, Status status) {
        var history = new History(MS_INVENTORY, status, message, LocalDateTime.now());
        return event.addHistory(history);
    }
}
