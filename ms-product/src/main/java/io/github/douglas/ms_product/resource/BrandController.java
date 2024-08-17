package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<?> registerBrand(@RequestBody BrandDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(brandService.registerBrand(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(brandService.getBrandById(request));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(brandService.getAllBrands());
    }

    @PatchMapping
    public ResponseEntity<?> updateBrand(@RequestBody BrandDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(brandService.updateBrand(request));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBrand(@RequestBody BrandDTO request) {
        brandService.deleteBrand(request);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
