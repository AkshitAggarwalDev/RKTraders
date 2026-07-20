package RKTraders.web.Repositories;

import RKTraders.web.Model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImage,Integer> {

        List<ProductImage> findByProductId(int productId);

    }
