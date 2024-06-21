package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.SupplierDTO;
import io.github.douglas.ms_product.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
