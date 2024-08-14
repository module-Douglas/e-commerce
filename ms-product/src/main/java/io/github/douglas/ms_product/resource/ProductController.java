package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;
import io.github.douglas.ms_product.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.created(
                productService.registerProduct(request)
        ).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductDetails(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllProductsLike(String name, String brand, UUID[] categories, UUID supplierId, Pageable pageRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAll(name, brand, categories, supplierId, pageRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@RequestBody RegisterProductDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(request));
    }
}
