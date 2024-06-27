package io.github.douglas.ms_accounts.dto;

import io.github.douglas.ms_accounts.model.entity.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String cpf,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Set<AddressDTO> addresses,
        Set<RoleDTO> roles
) {
    public UserDTO(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCpf(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getAddresses().stream()
                        .map(address -> new AddressDTO(
                                address.getId(),
                                address.getZipCode(),
                                address.getStreet(),
                                address.getNeighborhood(),
                                address.getComplement(),
                                address.getNumber(),
                                address.getCity(),
                                address.getState(),
                                address.getCreatedAt(),
                                address.getUpdatedAt()
                        )).collect(Collectors.toSet()),
                user.getRoles().stream()
                        .map(role -> new RoleDTO(
                                role.getId(),
                                role.getName(),
                                role.getCreatedAt()
                        )).collect(Collectors.toSet())
        );
    }
}
