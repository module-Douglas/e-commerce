package io.github.douglas.ms_product.model.repository;

import io.github.douglas.ms_product.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Boolean existsByName(String name);
    Boolean existsByEmail(String email);
    Boolean existsByCnpj(String cnpj);
    Boolean existsByPhoneNumber(String phoneNumber);

}
