package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.GenericIdHandler;
import io.github.douglas.ms_product.dto.SupplierDTO;
import io.github.douglas.ms_product.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<?> registerSupplier(@RequestBody SupplierDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supplierService.registerSupplier(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(supplierService.getSupplierDetails(request));
    }

    @PatchMapping
    public ResponseEntity<?> updateSupplier(@RequestBody SupplierDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(supplierService.updateSupplier(request));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSupplier(@RequestBody GenericIdHandler request) {
        supplierService.deleteSupplier(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
