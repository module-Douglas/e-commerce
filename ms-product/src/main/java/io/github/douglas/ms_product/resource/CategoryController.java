package io.github.douglas.ms_product.resource;

import io.github.douglas.ms_product.dto.CategoryDTO;
import io.github.douglas.ms_product.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id")UUID request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getCategoryById(request));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.getAll());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestBody CategoryDTO request) {
        categoryService.deleteCategory(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
