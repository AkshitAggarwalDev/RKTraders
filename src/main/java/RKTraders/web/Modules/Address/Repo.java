package RKTraders.web.Modules.Address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Repo extends JpaRepository<Entity, Integer> {

        List<Entity> findByCustomer(RKTraders.web.Modules.Customer.Entity customer);

        Optional<Entity> findByCustomerAndDefaultAddressTrue(RKTraders.web.Modules.Customer.Entity customer);

    }