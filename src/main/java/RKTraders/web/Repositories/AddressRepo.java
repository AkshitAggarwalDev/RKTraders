package RKTraders.web.Repositories;

import RKTraders.web.Model.Address;
import RKTraders.web.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Integer> {

        List<Address> findByCustomer(Customer customer);

        Optional<Address> findByCustomerAndDefaultAddressTrue(Customer customer);

    }