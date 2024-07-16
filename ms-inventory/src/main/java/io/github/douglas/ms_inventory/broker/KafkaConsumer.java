package io.github.douglas.ms_inventory.broker;

import io.github.douglas.ms_inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final InventoryService inventoryService;

    public KafkaConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.inventory-validation}"
    )
    private void consumeInventoryValidation(String payload) {
        inventoryService.orderUpdate(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.inventory-rollback}"
    )
    private void consumeInventoryRollback(String payload) {
        inventoryService.rollbackInventory(payload);
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.register-inventory}"
    )
    private void consumeRegisterInventory(String payload) {
        inventoryService.registerInventory(payload);
    }


}
