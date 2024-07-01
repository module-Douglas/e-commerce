package io.github.douglas.ms_accounts.model.repository;

import io.github.douglas.ms_accounts.model.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {

    Boolean existsByOrderIdAndTransactionId(String orderId, String transactionId);
    Optional<Validation> findByOrderIdAndTransactionId(String orderId, String transactionId);
}
