package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> registerProduct(@RequestBody ProductDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.registerProduct(request));
    }
}
