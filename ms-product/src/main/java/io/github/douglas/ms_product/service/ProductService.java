package io.github.douglas.ms_product.service;

import io.github.douglas.ms_product.dto.ProductDTO;

public interface ProductService {
    ProductDTO registerProduct(ProductDTO request);
}
