package io.github.douglas.ms_accounts.resource;

import io.github.douglas.ms_accounts.dto.RegisterAddressDTO;
import io.github.douglas.ms_accounts.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<?> registerAddress(@RequestBody RegisterAddressDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.registerAddress(request));
    }
}
