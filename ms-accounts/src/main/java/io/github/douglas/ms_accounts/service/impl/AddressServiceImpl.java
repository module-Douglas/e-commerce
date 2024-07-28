package io.github.douglas.ms_accounts.service.impl;

import io.github.douglas.ms_accounts.dto.AddressDTO;
import io.github.douglas.ms_accounts.dto.RegisterAddressDTO;
import io.github.douglas.ms_accounts.model.entity.Address;
import io.github.douglas.ms_accounts.model.entity.Account;
import io.github.douglas.ms_accounts.model.repository.AddressRepository;
import io.github.douglas.ms_accounts.model.repository.AccountRepository;
import io.github.douglas.ms_accounts.service.AddressService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;

    public AddressServiceImpl(AddressRepository addressRepository, AccountRepository accountRepository) {
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public AddressDTO registerAddress(RegisterAddressDTO request) {
        var user = getUser(request);
        var address = new Address(request);
        address.setUser(user);
        return new AddressDTO(addressRepository.save(address));
    }

    private Account getUser(RegisterAddressDTO request) {
        return accountRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with id: %s", request.userId())));
    }

}
