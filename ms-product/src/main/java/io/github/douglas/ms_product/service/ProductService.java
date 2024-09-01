package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    URI registerProduct(RegisterProductDTO request);

    void linkInventory(String payload);

    ProductDTO getProductDetails(UUID id);

    PageImpl<ProductDTO> getAll(String name, String brand, UUID[] categories, UUID supplierId, Pageable pageRequest);

    void deleteProduct(UUID id);

    ProductDTO updateProduct(RegisterProductDTO request);

    void updateProductStatus(String payload);
}
