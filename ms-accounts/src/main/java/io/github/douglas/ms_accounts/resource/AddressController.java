package io.github.douglas.ms_accounts.resource;

import io.github.douglas.ms_accounts.dto.AddressDTO;
import io.github.douglas.ms_accounts.dto.RemoveAddressDTO;
import io.github.douglas.ms_accounts.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<?> registerAddress(@RequestBody AddressDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.registerAddress(request));
    }

    @DeleteMapping
    public ResponseEntity<?> removeAddress(@RequestBody RemoveAddressDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.removeAddress(request));
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody AddressDTO request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(addressService.updateAddress(request));
    }
}
