package io.github.douglas.ms_inventory.broker;

import io.github.douglas.ms_inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    private final InventoryService inventoryService;

    public KafkaConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.inventory-validation}"
    )
    private void consumeInventoryValidation(String payload) {
        log.info("Receiving event {} from inventory-validation topic", payload);
        inventoryService.orderUpdate(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.inventory-rollback}"
    )
    private void consumeInventoryRollback(String payload) {
        log.info("Receiving event {} from inventory-rollback topic", payload);
        inventoryService.rollbackInventory(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.register-inventory}"
    )
    private void consumeRegisterInventory(String payload) {
        log.info("Receiving register event {} from register-inventory topic", payload);
        inventoryService.registerInventory(payload);
    }


}
