package RKTraders.web.Modules.Order;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface OrderItemRepo extends JpaRepository<OrderItemEntity, Integer> {

        List<OrderItemEntity> findByOrderId(int orderId);

    }

