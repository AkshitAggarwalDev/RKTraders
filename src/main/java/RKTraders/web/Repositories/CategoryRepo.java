package RKTraders.web.Repositories;


import RKTraders.web.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByName(String name);
    Optional<Category> findByNameIgnoreCase(String name);

}
