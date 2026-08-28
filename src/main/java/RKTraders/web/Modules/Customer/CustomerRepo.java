package RKTraders.web.Modules.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity,Integer> {
    Optional<CustomerEntity> findByEmail(String email);
}
