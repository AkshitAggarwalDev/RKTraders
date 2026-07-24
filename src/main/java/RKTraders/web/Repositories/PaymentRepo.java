package RKTraders.web.Repositories;

import RKTraders.web.Model.Customer;
import RKTraders.web.Model.Payment;
import RKTraders.web.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment,Integer> {
    Optional<Payment> findByPaymentId(String paymentId);

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByCustomer(Customer customer);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

}
