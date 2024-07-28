package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.RegisterProductDTO;
import io.github.douglas.ms_product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> registerProduct(@RequestBody RegisterProductDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.registerProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductDetails(id));
    }
}
