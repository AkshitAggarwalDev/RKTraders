package RKTraders.web.Modules.Address;

import RKTraders.web.Modules.Customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<AddressEntity, Integer> {

        List<AddressEntity> findByCustomer(CustomerEntity customer);

        Optional<AddressEntity> findByCustomerAndDefaultAddressTrue(CustomerEntity customer);

    }