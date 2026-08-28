package RKTraders.web.Modules.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @PostMapping("addAddress")
    public ResponseEntity<AddressEntity> addAddress(@RequestBody AddressEntity address,
                                                    Authentication authentication) {

        return ResponseEntity.ok(
                addressService.addAddress(address, authentication.getName())
        );

    }

    @GetMapping("getMyAddress")
    public ResponseEntity<List<AddressEntity>> getMyAddresses(Authentication authentication) {

        return ResponseEntity.ok(
                addressService.getMyAddresses(authentication.getName())
        );

    }

    @GetMapping("{addressId}")
    public ResponseEntity<AddressEntity> getAddressById(@PathVariable Integer addressId,
                                                        Authentication authentication) {

        return ResponseEntity.ok(
                addressService.getAddressById(addressId, authentication.getName())
        );

    }

    @PutMapping("updateAddress/{addressId}")
    public ResponseEntity<AddressEntity> updateAddress(@PathVariable Integer addressId,
                                                       @RequestBody AddressEntity address,
                                                       Authentication authentication) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        addressId,
                        address,
                        authentication.getName()
                )
        );

    }

    @DeleteMapping("delete/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId,
                                                Authentication authentication) {

        return ResponseEntity.ok(
                addressService.deleteAddress(
                        addressId,
                        authentication.getName()
                )
        );

    }

    @PatchMapping("default/{addressId}")
    public ResponseEntity<AddressEntity> setDefaultAddress(
            @PathVariable Integer addressId,
            Authentication authentication) {

        return ResponseEntity.ok(
                addressService.setDefaultAddress(
                        addressId,
                        authentication.getName()
                )
        );

    }
}
