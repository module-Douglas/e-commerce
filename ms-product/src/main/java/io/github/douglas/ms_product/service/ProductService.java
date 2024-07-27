package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;

public interface ProductService {
    ProductDTO registerProduct(RegisterProductDTO request);

    void linkInventory(String payload);

    ProductDTO getProductDetails(String id);
}
