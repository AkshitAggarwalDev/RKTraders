package RKTraders.web.Repositories;
import RKTraders.web.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

    @Repository
    public interface ReviewRepo extends JpaRepository<Review, Integer> {

        // All Reviews of a Product
        List<Review> findByProductId(Integer productId);

        // All Reviews of a Customer
        List<Review> findByCustomerId(Integer customerId);

        // Check if customer already reviewed the product
        Optional<Review> findByCustomerIdAndProductId(Integer customerId,
                                                      Integer productId);

        // Duplicate Check
        boolean existsByCustomerIdAndProductId(Integer customerId,
                                               Integer productId);

    }
