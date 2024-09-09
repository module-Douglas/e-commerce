package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.GenericIdHandler;
import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ProductDTO registerProduct(RegisterProductDTO request);

    void linkInventory(String payload);

    ProductDTO getProductDetails(UUID request);

    PageImpl<ProductDTO> getAll(String name, String brand, UUID[] categories, UUID supplierId, Pageable pageRequest);

    void deleteProduct(GenericIdHandler request);

    ProductDTO updateProduct(ProductDTO request);

    void updateProductStatus(String payload);
}
