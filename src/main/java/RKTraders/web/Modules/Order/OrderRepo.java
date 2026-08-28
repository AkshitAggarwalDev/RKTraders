package RKTraders.web.Modules.Order;


import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Owner.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findAll();
        List<OrderEntity> findByCustomerId(int customerId);
    List<OrderEntity> findByOrderStatus(OrderStatus orderStatus);
    long countByCustomer(CustomerEntity customer);
    List<OrderEntity> findTop10ByOrderByOrderDateDesc();

    }