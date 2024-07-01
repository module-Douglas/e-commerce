package io.github.douglas.ms_accounts.dto.event;


import io.github.douglas.ms_accounts.enums.Sources;
import io.github.douglas.ms_accounts.enums.Status;
import io.github.douglas.ms_accounts.model.entity.Address;
import io.github.douglas.ms_accounts.model.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public record Event(
        String id,
        String transactionId,
        AccountDetails accountDetails,
        DeliveryAddress deliveryAddress,
        BigDecimal totalAmount,
        Long totalItems,
        LocalDateTime createdAt,
        Sources source,
        Status status,
        Set<Product> products,
        Set<History> historic
) {
    public Event addHistory(History history) {
        var newHistoric = new HashSet<History>();
        if (this.historic == null) {
            newHistoric.add(history);
            return new Event(id, transactionId, accountDetails, deliveryAddress, totalAmount, totalItems, createdAt, source, status, products, newHistoric);
        }
        this.historic.add(history);
        return this;
    }

    public Event setAccountDetails(Address address, User user) {
        return new Event(id, transactionId, AccountDetails.setAccountDetails(user), DeliveryAddress.setAddress(address), totalAmount, totalItems, createdAt, source, status, products, historic);
    }
}
