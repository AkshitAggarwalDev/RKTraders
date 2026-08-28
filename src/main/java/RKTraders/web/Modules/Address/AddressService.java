package RKTraders.web.Modules.Address;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
    public class AddressService {

        @Autowired
        private Repo addressRepo;

        @Autowired
        private RKTraders.web.Modules.Customer.Repo customerRepo;

        public Entity addAddress(Entity address, String email) {

            RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            address.setCustomer(customer);

            if (address.getDefaultAddress() == null) {
                address.setDefaultAddress(false);
            }

            if (address.getDefaultAddress()) {

                Optional<Entity> defaultAddress =
                        addressRepo.findByCustomerAndDefaultAddressTrue(customer);

                if (defaultAddress.isPresent()) {

                    Entity oldDefault = defaultAddress.get();

                    oldDefault.setDefaultAddress(false);

                    addressRepo.save(oldDefault);

                }

            }

            return addressRepo.save(address);

        }

    public List<Entity> getMyAddresses(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return addressRepo.findByCustomer(customer);

    }

    public Entity getAddressById(Integer addressId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        return address;

    }

    public Entity updateAddress(Integer addressId,
                                Entity updatedAddress,
                                String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        address.setFullName(updatedAddress.getFullName());
        address.setPhoneNumber(updatedAddress.getPhoneNumber());
        address.setHouseNumber(updatedAddress.getHouseNumber());
        address.setStreet(updatedAddress.getStreet());
        address.setNearByLoc(updatedAddress.getNearByLoc());
        address.setCity(updatedAddress.getCity());
        address.setState(updatedAddress.getState());
        address.setPincode(updatedAddress.getPincode());
        address.setCountry(updatedAddress.getCountry());
        address.setAddressType(updatedAddress.getAddressType());

        return addressRepo.save(address);

    }

    public String deleteAddress(Integer addressId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        addressRepo.delete(address);

        return "Address Deleted Successfully";

    }

    public Entity setDefaultAddress(Integer addressId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        List<Entity> addresses = addressRepo.findByCustomer(customer);

        for (Entity a : addresses) {

            a.setDefaultAddress(false);

            addressRepo.save(a);

        }

        address.setDefaultAddress(true);

        return addressRepo.save(address);

    }

    }
