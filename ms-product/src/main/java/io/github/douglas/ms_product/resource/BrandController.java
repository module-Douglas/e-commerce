package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.BrandDTO;
import io.github.douglas.ms_product.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
