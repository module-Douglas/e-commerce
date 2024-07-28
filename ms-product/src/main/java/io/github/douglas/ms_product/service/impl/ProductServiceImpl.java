package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.broker.KafkaProducer;
import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;
import io.github.douglas.ms_product.dto.RegisterInventoryDTO;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;
import io.github.douglas.ms_product.model.entity.Supplier;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.model.repository.ProductRepository;
import io.github.douglas.ms_product.model.repository.SupplierRepository;
import io.github.douglas.ms_product.service.ProductService;
import io.github.douglas.ms_product.utils.JsonUtil;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;

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
    public URI registerProduct(RegisterProductDTO request) {
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

        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(product.getId()).toUri();
    }

    @Override
    public void linkInventory(String payload) {
        var link = jsonUtil.toLinkInventory(payload);
        var product = productRepository.findById(link.inventoryId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s", link.productId())));

        product.setInventoryId(link.inventoryId());
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProductDetails(UUID id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s", id)));

        return new ProductDTO(product);
    }

    @Override
    public PageImpl<ProductDTO> findByName(String name, Pageable pageRequest) {
        List<ProductDTO> response = productRepository
                .findAll(Example.of(new Product(name),
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withIgnoreNullValues()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)), pageRequest)
                .stream().map(ProductDTO::new).toList();

        return new PageImpl<>(response, pageRequest, response.size());

    }

    private Set<Category> getCategories(Set<UUID> categories) {
         Set<Category> response = new HashSet<>();
         categories.forEach(
                 category -> {
                     response.add(categoryRepository.findById(category)
                             .orElseThrow(() -> new ResourceNotFoundException(format("Category not found with id: %s", category))));
                 }
         );

         return response;
    }

    private Supplier getSupplier(String id) {
        return supplierRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException(format("Supplier not found with id: %s", id)));
    }
}
