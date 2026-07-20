package RKTraders.web.Repositories;


import RKTraders.web.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

        List<OrderItem> findByOrderId(int orderId);

    }

