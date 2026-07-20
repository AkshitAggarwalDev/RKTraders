package RKTraders.web.Repositories;


import RKTraders.web.Model.Customer;
import RKTraders.web.Model.Order;
import RKTraders.web.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface OrderRepo extends JpaRepository<Order, Integer> {
    List<Order> findAll();
        List<Order> findByCustomerId(int customerId);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    long countByCustomer(Customer customer);
    List<Order> findTop10ByOrderByOrderDateDesc();

    }