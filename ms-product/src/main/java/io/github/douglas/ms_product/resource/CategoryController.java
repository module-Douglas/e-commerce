package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<?> registerCategory(@RequestBody CategoryDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.registerCategory(request));
    }
}
