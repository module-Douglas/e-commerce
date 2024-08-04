package io.github.douglas.ms_accounts.service;

import io.github.douglas.ms_accounts.dto.AccountDTO;
import io.github.douglas.ms_accounts.dto.AddressDTO;
import io.github.douglas.ms_accounts.dto.RemoveAddressDTO;

public interface AddressService {

    AddressDTO registerAddress(AddressDTO request);

    AccountDTO removeAddress(RemoveAddressDTO request);

    AddressDTO updateAddress(AddressDTO request);
}
