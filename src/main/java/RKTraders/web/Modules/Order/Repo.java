package RKTraders.web.Modules.Order;


import RKTraders.web.Modules.Owner.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface Repo extends JpaRepository<Entity, Integer> {
    List<Entity> findAll();
        List<Entity> findByCustomerId(int customerId);
    List<Entity> findByOrderStatus(OrderStatus orderStatus);
    long countByCustomer(RKTraders.web.Modules.Customer.Entity customer);
    List<Entity> findTop10ByOrderByOrderDateDesc();

    }