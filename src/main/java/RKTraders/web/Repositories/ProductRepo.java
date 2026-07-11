package RKTraders.web.Repositories;

import RKTraders.web.Model.Category;
import RKTraders.web.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    long countByCategory(Category category);
    List<Product> findByCategory(Category category);

}
