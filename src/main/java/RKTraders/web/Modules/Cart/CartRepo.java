package RKTraders.web.Modules.Cart;

import RKTraders.web.Modules.Customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepo extends JpaRepository<CartEntity, Integer> {

        Optional<CartEntity> findByCustomerId(int customerId);
    Optional<CartEntity> findByCustomer(CustomerEntity customer);



    }
