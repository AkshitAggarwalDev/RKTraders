package RKTraders.web.Modules.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface Repo extends JpaRepository<Entity, Integer> {

        Optional<Entity> findByCustomerId(int customerId);
    Optional<Entity> findByCustomer(RKTraders.web.Modules.Customer.Entity customer);



    }
