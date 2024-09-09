package io.github.douglas.ms_product.service.impl;

import io.github.douglas.ms_product.broker.KafkaProducer;
import io.github.douglas.ms_product.config.exception.ValidationException;
import io.github.douglas.ms_product.dto.*;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.douglas.ms_product.enums.Status.AVAILABLE;
import static io.github.douglas.ms_product.enums.Status.OUT_OF_STOCK;
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

    @CacheEvict(value = "pageProducts", allEntries = true)
    @Override
    public ProductDTO registerProduct(RegisterProductDTO request) {
        checkName(request.name());
        var categories = getCategories(request.categories());
        var supplier = getSupplier(request.supplierId());
        var brand = getOrRegisterBrand(request.brand().toUpperCase());

        var product = productRepository.save(new Product(
                request.name().toUpperCase(),
                request.description(),
                brand,
                request.unitValue(),
                request.stockAmount() > 0 ? AVAILABLE : OUT_OF_STOCK,
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

        return new ProductDTO(product);
    }

    @Override
    public void linkInventory(String payload) {
        var link = jsonUtil.toLinkInventory(payload);
        var product = productRepository.findById(link.productId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", link.productId())));

        product.setInventoryId(link.inventoryId());
        productRepository.save(product);
    }

    @Cacheable(value = "product", key = "#request")
    @Override
    public ProductDTO getProductDetails(UUID request) {
        var product = productRepository.findById(request)
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", request)));

        return new ProductDTO(product);
    }

    @Cacheable(value = "pageProducts")
    @Override
    public PageImpl<ProductDTO> getAll(String name, String brand, UUID[] categories, UUID supplierId, Pageable pageRequest) {
        Product product = new Product();
        product.setName(name);
        product.setBrand(new Brand(brand));
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
    public void deleteProduct(GenericIdHandler request) {
        var product = productRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", request.id())));
        productRepository.delete(product);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO request) {
        var product = productRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", request.id())));

        product.setName(request.name());
        product.setBrand(getBrand(request.brand()));
        product.setDescription(request.description());
        product.setUnitValue(request.unitValue());
        var categories = getCategories(
                request.categories().stream()
                        .map(CategoryDTO::id)
                        .collect(Collectors.toSet())
        );
        product.setCategories(categories);

        kafkaProducer.sendInventoryUpdate(
                jsonUtil.toJson(new UpdateInventoryDTO(product.getInventoryId(), product.getId(), request.unitValue()))
        );
        return new ProductDTO(
                productRepository.save(product)
        );
    }

    @Override
    public void updateProductStatus(String payload) {
        var inventory = jsonUtil.toUpdateStatus(payload);
        var product = productRepository.findById(inventory.productId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Product not found with id: %s.", inventory.productId())));

        if (!product.getInventoryId().equals(inventory.inventoryId()))
            throw new ValidationException(format("Inventory id from update request doesn't match with specified product: %s.", product.getId()));

        product.setStatus(inventory.status());
        productRepository.save(product);
    }

    private void checkName(String name) {
        name = name.toUpperCase();
        if (productRepository.existsByName(name))
            throw new DataIntegrityViolationException(format("Product with name %s is already registered.", name));
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
