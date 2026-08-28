package RKTraders.web.Modules.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Repo extends JpaRepository<Entity,Integer> {
    long countByCategory(RKTraders.web.Modules.Category.Entity category);
    List<Entity> findByCategory(RKTraders.web.Modules.Category.Entity category);
        List<Entity> findByNameContainingIgnoreCase(String name);
    List<Entity> findByBrandIgnoreCase(String brand);
    List<Entity> findByPriceBetween(double minPrice, double maxPrice);
    List<Entity> findByStock(int stock);
    List<Entity> findByStockLessThanEqual(int stock);




}
