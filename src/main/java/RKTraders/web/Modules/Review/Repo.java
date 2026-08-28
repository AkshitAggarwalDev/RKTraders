package RKTraders.web.Modules.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

    @Repository
    public interface Repo extends JpaRepository<Entity, Integer> {

        // All Reviews of a Product
        List<Entity> findByProductId(Integer productId);

        // All Reviews of a Customer
        List<Entity> findByCustomerId(Integer customerId);

        // Check if customer already reviewed the product
        Optional<Entity> findByCustomerIdAndProductId(Integer customerId,
                                                      Integer productId);

        // Duplicate Check
        boolean existsByCustomerIdAndProductId(Integer customerId,
                                               Integer productId);

    }
