package RKTraders.web.Repositories;

import RKTraders.web.Model.Category;
import RKTraders.web.Model.Customer;
import RKTraders.web.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepo extends JpaRepository<Owner, Integer> {
    Optional<Owner> findByOwnerEmail(String email);
}
