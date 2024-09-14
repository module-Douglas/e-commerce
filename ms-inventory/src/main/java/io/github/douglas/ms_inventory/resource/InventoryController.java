package io.github.douglas.ms_inventory.resource;

import io.github.douglas.ms_inventory.dto.UpdateStockAmountDTO;
import io.github.douglas.ms_inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getInventoryDetailsByProductId(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.findByProductId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryDetails(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.findById(id));
    }

    @PatchMapping
    public ResponseEntity<?> updateInventoryStock(@RequestBody UpdateStockAmountDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.updateStockAmount(request));
    }

}
