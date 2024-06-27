package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.AddressDTO;
import io.github.douglas.ms_accounts.dto.RegisterAddressDTO;

public interface AddressService {
    AddressDTO registerAddress(RegisterAddressDTO request);
}
