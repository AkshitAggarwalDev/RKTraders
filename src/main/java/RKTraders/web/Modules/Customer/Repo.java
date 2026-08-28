package RKTraders.web.Modules.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface Repo extends JpaRepository<Entity,Integer> {
    Optional<Entity> findByEmail(String email);
}
