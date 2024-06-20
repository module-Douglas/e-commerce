package io.github.douglas.ms_order.model.entity;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {

    private Long addressId;
    private String zipCode;
    private String street;
    private String neighborhood;
    private String complement;
    private String number;
    private String city;
    private String state;

}
