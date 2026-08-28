package RKTraders.web.Modules.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Repo extends JpaRepository<Entity,Integer> {
    Optional<Entity> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByName(String name);
    Optional<Entity> findByNameIgnoreCase(String name);


}
