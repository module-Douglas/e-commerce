package io.github.douglas.ms_order.model.entity;

import io.github.douglas.ms_order.enums.Sources;
import io.github.douglas.ms_order.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Order {

    @Id
    private String id;
    private String transactionId;
    private Long accountId;
    private Long addressId;
    private BigDecimal totalAmount;
    private Long totalItems;
    private LocalDateTime createdAt;
    private Sources source;
    private Status status;
    private Set<OrderProduct> products;
    private Set<History> historic;
}
