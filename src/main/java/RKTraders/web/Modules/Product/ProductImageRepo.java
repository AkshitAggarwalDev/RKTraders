package RKTraders.web.Modules.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImageEntity,Integer> {

        List<ProductImageEntity> findByProductId(int productId);

    }
