package RKTraders.web.Repositories;

import RKTraders.web.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

        List<CartItem> findByCartId(int cartId);

        Optional<CartItem> findByCartIdAndProductId(int cartId, int productId);

    }