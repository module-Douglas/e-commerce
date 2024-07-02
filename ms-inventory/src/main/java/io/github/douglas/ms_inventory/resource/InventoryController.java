package io.github.douglas.ms_inventory.resource;

import io.github.douglas.ms_inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getByProductId(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(inventoryService.findByProductId(id));
    }

}
