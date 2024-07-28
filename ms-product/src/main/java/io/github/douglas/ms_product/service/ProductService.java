package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.ProductDTO;
import io.github.douglas.ms_product.dto.RegisterProductDTO;

import java.net.URI;
import java.util.UUID;

public interface ProductService {
    URI registerProduct(RegisterProductDTO request);

    void linkInventory(String payload);

    ProductDTO getProductDetails(UUID id);
}
