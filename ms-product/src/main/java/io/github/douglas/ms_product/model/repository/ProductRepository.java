package io.github.douglas.ms_product.model.repository;

import io.github.douglas.ms_product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Boolean existsByName(String name);

}
