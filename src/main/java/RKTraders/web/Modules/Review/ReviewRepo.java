package RKTraders.web.Modules.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

    @Repository
    public interface ReviewRepo extends JpaRepository<ReviewEntity, Integer> {

        // All Reviews of a Product
        List<ReviewEntity> findByProductId(Integer productId);

        // All Reviews of a Customer
        List<ReviewEntity> findByCustomerId(Integer customerId);

        // Check if customer already reviewed the product
        Optional<ReviewEntity> findByCustomerIdAndProductId(Integer customerId,
                                                            Integer productId);

        // Duplicate Check
        boolean existsByCustomerIdAndProductId(Integer customerId,
                                               Integer productId);

    }
