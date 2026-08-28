package RKTraders.web.Modules.Owner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OwnerRepo extends JpaRepository<OwnerEntity,Integer> {
    Optional<OwnerEntity> findByOwnerEmail(String email);
}
