package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.broker.KafkaProducer;
import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;
import io.github.douglas.ms_product.dto.RegisterInventoryDTO;
import io.github.douglas.ms_product.dto.UpdateInventoryDTO;
import io.github.douglas.ms_product.model.entity.Brand;
import io.github.douglas.ms_product.model.entity.Category;
import io.github.douglas.ms_product.model.entity.Product;
import io.github.douglas.ms_product.model.entity.Supplier;
import io.github.douglas.ms_product.model.repository.BrandRepository;
import io.github.douglas.ms_product.model.repository.CategoryRepository;
import io.github.douglas.ms_product.model.repository.ProductRepository;
import io.github.douglas.ms_product.model.repository.SupplierRepository;
import io.github.douglas.ms_product.service.ProductService;
import io.github.douglas.ms_product.utils.JsonUtil;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final BrandRepository brandRepository;
    private final KafkaProducer kafkaProducer;
    private final JsonUtil jsonUtil;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SupplierRepository supplierRepository,
            BrandRepository brandRepository,
            KafkaProducer kafkaProducer,
            JsonUtil jsonUtil) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.brandRepository = brandRepository;
        this.kafkaProducer = kafkaProducer;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public URI registerProduct(RegisterProductDTO request) {
        var categories = getCategories(request.categories());
        var supplier = getSupplier(request.supplierId());
        var brand = getOrRegisterBrand(request.brand().toUpperCase());

        var product = productRepository.save(new Product(
                request.name().toUpperCase(),
                request.description(),
                brand,
                request.unitValue(),
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
        var product = productRepository.findById(link.productId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", link.productId())));

        product.setInventoryId(link.inventoryId());
        productRepository.save(product);
    }

    @Cacheable(value = "product", key = "#id")
    @Override
    public ProductDTO getProductDetails(UUID id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", id)));

        return new ProductDTO(product);
    }

    @Cacheable(value = "pageProducts")
    @Override
    public PageImpl<ProductDTO> getAll(String name, String brand, UUID[] categories, UUID supplierId, Pageable pageRequest) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(getBrand(brand));
        product.setSupplier(new Supplier(supplierId));

        if(!(categories == null)) {
            product.setCategories(
                    getCategories(Arrays.stream(categories).collect(Collectors.toSet()))
            );
        }
        List<ProductDTO> response = productRepository
                .findAll(Example.of(product,
                        ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withIgnoreNullValues()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)), pageRequest)
                .stream().map(ProductDTO::new).toList();

        return new PageImpl<>(response, pageRequest, response.size());
    }

    @Override
    public void deleteProduct(UUID id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", id)));
        productRepository.delete(product);
    }

    @Override
    public ProductDTO updateProduct(RegisterProductDTO request) {
        var product = productRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", request.id())));

        product.setName(request.name());
        product.setBrand(getBrand(request.brand()));
        product.setDescription(request.description());
        product.setUnitValue(request.unitValue());

        kafkaProducer.sendInventoryUpdate(
                jsonUtil.toJson(new UpdateInventoryDTO(product.getId(), request.unitValue()))
        );
        return new ProductDTO(
                productRepository.save(product)
        );
    }

    private Set<Category> getCategories(Set<UUID> categories) {
         Set<Category> response = new HashSet<>();
         categories.forEach(
                 category -> response.add(categoryRepository.findById(category)
                         .orElseThrow(() -> new ResourceNotFoundException(format("Category not found with id: %s.", category))))
         );

         return response;
    }

    private Supplier getSupplier(String id) {
        return supplierRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException(format("Supplier not found with id: %s.", id)));
    }

    private Brand getOrRegisterBrand(String brand) {
        if(brandRepository.existsByName(brand)) {
            return brandRepository.findByName(brand)
                    .orElseThrow(() -> new ResourceNotFoundException(format("Brand %s not found.", brand)));
        }

        return brandRepository.save(new Brand(brand));
    }

    private Brand getBrand(String brand) {
        return brandRepository.findByName(brand)
                .orElseThrow(() -> new ResourceNotFoundException(format("Brand %s not found.", brand)));
    }

}
