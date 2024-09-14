package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.GenericIdHandler;
import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;
import io.github.douglas.ms_product.service.ProductService;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> getProductDetails(@PathVariable("id") UUID request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductDetails(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllProductsLike(String name, String brand, UUID[] categories, UUID supplierId, Pageable pageRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getAll(name, brand, categories, supplierId, pageRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestBody GenericIdHandler request) {
        productService.deleteProduct(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.updateProduct(request));
    }
}
