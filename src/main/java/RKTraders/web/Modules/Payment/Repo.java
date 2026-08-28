package RKTraders.web.Modules.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Repo extends JpaRepository<Entity,Integer> {
    Optional<Entity> findByPaymentId(String paymentId);

    Optional<Entity> findByTransactionId(String transactionId);

    List<Entity> findByCustomer(RKTraders.web.Modules.Customer.Entity customer);

    List<Entity> findByPaymentStatus(PaymentStatusEnum paymentStatus);

}
