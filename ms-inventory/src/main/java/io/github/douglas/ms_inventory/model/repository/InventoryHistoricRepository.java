package io.github.douglas.ms_inventory.model.repository;

import io.github.douglas.ms_inventory.model.entity.InventoryHistoric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryHistoricRepository extends JpaRepository<InventoryHistoric, UUID> {

    Boolean existsByOrderIdAndTransactionId(String orderId, String transactionId);
    List<InventoryHistoric> findByOrderIdAndTransactionId(String orderId, String transactionId);

}
