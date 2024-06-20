package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String transactionId;
    private AccountDetails accountDetails;
    private DeliveryAddress deliveryAddress;
    private BigDecimal totalAmount;
    private Long totalItems;
    private LocalDateTime createdAt;
    private Sources source;
    private Status status;
    private Set<Product> products = new HashSet<>();
    private Set<History> historic;

}
