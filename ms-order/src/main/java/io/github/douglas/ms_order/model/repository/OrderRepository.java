package io.github.douglas.ms_order.model.repository;

import io.github.douglas.ms_order.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
