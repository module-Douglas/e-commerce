package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.config.exception.ValidationException;
import io.github.douglas.ms_accounts.dto.AccountDTO;
import io.github.douglas.ms_accounts.dto.AddressDTO;
import io.github.douglas.ms_accounts.dto.RemoveAddressDTO;
import io.github.douglas.ms_accounts.model.entity.Address;
import io.github.douglas.ms_accounts.model.entity.Account;
import io.github.douglas.ms_accounts.model.repository.AddressRepository;
import io.github.douglas.ms_accounts.model.repository.AccountRepository;
import io.github.douglas.ms_accounts.service.AddressService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;

    public AddressServiceImpl(AddressRepository addressRepository, AccountRepository accountRepository) {
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public AddressDTO registerAddress(AddressDTO request) {
        var user = getAccount(request.accountId());
        var address = new Address(request);
        address.setAccount(user);
        return new AddressDTO(addressRepository.save(address));
    }

    @Override
    public AccountDTO removeAddress(RemoveAddressDTO request) {
        var user = getAccount(request.accountId());
        var address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new ResourceNotFoundException(format("Address not found with id: %s", request.addressId())));

        if (!user.getAddresses().contains(address)) {
            throw new ValidationException(format("Account %s doesn't contains Address %s", request.accountId(), request.addressId()));
        }

        user.getAddresses().remove(address);
        addressRepository.delete(address);
        accountRepository.save(user);
        return new AccountDTO(
                getAccount(request.accountId())
        );
    }

    @Override
    public AddressDTO updateAddress(AddressDTO request) {
        var address = addressRepository.findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(format("Address not found with id: %s", request.id())));

        address.setZipCode(request.zipCode());
        address.setStreet(request.street());
        address.setNeighborhood(request.neighborhood());
        address.setComplement(request.complement());
        address.setNumber(request.number());
        address.setCity(request.city());
        address.setState(request.state());

        return new AddressDTO(
                addressRepository.save(address)
        );
    }

    private Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(format("User not found with id: %s", accountId)));
    }

}
