package io.github.douglas.ms_product.model.repository;

import io.github.douglas.ms_product.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {

    Boolean existsByName(String name);
    Optional<Brand> findByName(String name);

}
