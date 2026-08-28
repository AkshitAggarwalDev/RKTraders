package RKTraders.web.Modules.Address;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Customer.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
    public class AddressService {

        @Autowired
        private AddressRepo addressRepo;

        @Autowired
        private CustomerRepo customerRepo;

        public AddressEntity addAddress(AddressEntity address, String email) {

            CustomerEntity customer = customerRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

            address.setCustomer(customer);

            if (address.getDefaultAddress() == null) {
                address.setDefaultAddress(false);
            }

            if (address.getDefaultAddress()) {

                Optional<AddressEntity> defaultAddress =
                        addressRepo.findByCustomerAndDefaultAddressTrue(customer);

                if (defaultAddress.isPresent()) {

                    AddressEntity oldDefault = defaultAddress.get();

                    oldDefault.setDefaultAddress(false);

                    addressRepo.save(oldDefault);

                }

            }

            return addressRepo.save(address);

        }

    public List<AddressEntity> getMyAddresses(String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return addressRepo.findByCustomer(customer);

    }

    public AddressEntity getAddressById(Integer addressId, String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        AddressEntity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        return address;

    }

    public AddressEntity updateAddress(Integer addressId,
                                       AddressEntity updatedAddress,
                                       String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        AddressEntity address = addressRepo.findById(addressId)
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

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        AddressEntity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        addressRepo.delete(address);

        return "Address Deleted Successfully";

    }

    public AddressEntity setDefaultAddress(Integer addressId, String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        AddressEntity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized");
        }

        List<AddressEntity> addresses = addressRepo.findByCustomer(customer);

        for (AddressEntity a : addresses) {

            a.setDefaultAddress(false);

            addressRepo.save(a);

        }

        address.setDefaultAddress(true);

        return addressRepo.save(address);

    }

    }
