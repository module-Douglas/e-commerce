package io.github.douglas.ms_order.model.repository;

import io.github.douglas.ms_order.model.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findAllByAccountDetails_UserId(UUID userId, Pageable pageable);

}
