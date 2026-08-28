package RKTraders.web.Modules.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Integer> {
    Optional<CategoryEntity> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByName(String name);
    Optional<CategoryEntity> findByNameIgnoreCase(String name);


}
