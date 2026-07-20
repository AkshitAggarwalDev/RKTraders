package RKTraders.web.Service;

import RKTraders.web.Model.Address;
import RKTraders.web.Model.Customer;
import RKTraders.web.Repositories.AddressRepo;
import RKTraders.web.Repositories.CustomerRepo;
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

        public Address addAddress(Address address, String email) {

            Customer customer = customerRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            address.setCustomer(customer);

            if (address.getDefaultAddress() == null) {
                address.setDefaultAddress(false);
            }

            if (address.getDefaultAddress()) {

                Optional<Address> defaultAddress =
                        addressRepo.findByCustomerAndDefaultAddressTrue(customer);

                if (defaultAddress.isPresent()) {

                    Address oldDefault = defaultAddress.get();

                    oldDefault.setDefaultAddress(false);

                    addressRepo.save(oldDefault);

                }

            }

            return addressRepo.save(address);

        }

    public List<Address> getMyAddresses(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return addressRepo.findByCustomer(customer);

    }

    public Address getAddressById(Integer addressId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        return address;

    }

    public Address updateAddress(Integer addressId,
                                 Address updatedAddress,
                                 String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new RuntimeException("Unauthorized");
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

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        addressRepo.delete(address);

        return "Address Deleted Successfully";

    }

    public Address setDefaultAddress(Integer addressId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new RuntimeException("Unauthorized");
        }

        List<Address> addresses = addressRepo.findByCustomer(customer);

        for (Address a : addresses) {

            a.setDefaultAddress(false);

            addressRepo.save(a);

        }

        address.setDefaultAddress(true);

        return addressRepo.save(address);

    }

    }
