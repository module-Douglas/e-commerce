package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.broker.KafkaProducer;
import io.github.douglas.ms_product.config.exception.ResourceNotFoundException;
import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterInventoryDTO;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;
import io.github.douglas.ms_product.model.entity.Supplier;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.model.repository.ProductRepository;
import io.github.douglas.ms_product.model.repository.SupplierRepository;
import io.github.douglas.ms_product.service.ProductService;
import io.github.douglas.ms_product.utils.JsonUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            KafkaProducer kafkaProducer,
            JsonUtil jsonUtil) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.kafkaProducer = kafkaProducer;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public ProductDTO registerProduct(ProductDTO request) {
        var categories = getCategories(request.categories());
        var supplier = getSupplier(request.supplierId());

        var product = productRepository.save(new Product(
                request.name(),
                request.description(),
                request.brand(),
                categories,
                supplier
        ));

        var registerInventory = new RegisterInventoryDTO(
                product.getId(),
                BigDecimal.valueOf(request.unitValue().doubleValue()),
                request.stockAmount()
        );
        kafkaProducer.sendInventoryRegister(
                jsonUtil.toJson(registerInventory)
        );

        return request.generateDTO(product);
    }

    @Override
    public void linkInventory(String payload) {
        var link = jsonUtil.toLinkInventory(payload);
        var product = productRepository.findById(UUID.fromString(link.productId()))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product not found with id: %s", link.productId())));

        product.setInventoryId(link.inventoryId());
        productRepository.save(product);
    }

    private Set<Category> getCategories(Set<Long> categories) {
         Set<Category> response = new HashSet<>();
         categories.forEach(
                 category -> {
                     response.add(categoryRepository.findById(category)
                             .orElseThrow(() -> new ResourceNotFoundException(String
                                     .format("Category not found with id: %s", category))));
                 }
         );

         return response;
    }

    private Supplier getSupplier(String id) {
        return supplierRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Supplier not found with id: %s", id)));
    }
}
