package RKTraders.web.Modules.Product;

import RKTraders.web.Modules.Category.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Integer> {
    long countByCategory(CategoryEntity category);
    List<ProductEntity> findByCategory(CategoryEntity category);
        List<ProductEntity> findByNameContainingIgnoreCase(String name);
    List<ProductEntity> findByBrandIgnoreCase(String brand);
    List<ProductEntity> findByPriceBetween(double minPrice, double maxPrice);
    List<ProductEntity> findByStock(int stock);
    List<ProductEntity> findByStockLessThanEqual(int stock);




}
