package RKTraders.web.Modules.Address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Address")
public class Controller {

    @Autowired
    AddressService addressService;

    @PostMapping("addAddress")
    public ResponseEntity<Entity> addAddress(@RequestBody Entity address,
                                             Authentication authentication) {

        return ResponseEntity.ok(
                addressService.addAddress(address, authentication.getName())
        );

    }

    @GetMapping("getMyAddress")
    public ResponseEntity<List<Entity>> getMyAddresses(Authentication authentication) {

        return ResponseEntity.ok(
                addressService.getMyAddresses(authentication.getName())
        );

    }

    @GetMapping("{addressId}")
    public ResponseEntity<Entity> getAddressById(@PathVariable Integer addressId,
                                                 Authentication authentication) {

        return ResponseEntity.ok(
                addressService.getAddressById(addressId, authentication.getName())
        );

    }

    @PutMapping("updateAddress/{addressId}")
    public ResponseEntity<Entity> updateAddress(@PathVariable Integer addressId,
                                                @RequestBody Entity address,
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
    public ResponseEntity<Entity> setDefaultAddress(
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
