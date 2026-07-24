package RKTraders.web.Repositories;

import RKTraders.web.Model.Cart;
import RKTraders.web.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

        Optional<Cart> findByCustomerId(int customerId);
    Optional<Cart> findByCustomer(Customer customer);



    }
