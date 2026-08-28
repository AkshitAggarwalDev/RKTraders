package RKTraders.web.Modules.Payment;

import RKTraders.web.Modules.Customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<PaymentEntity,Integer> {
    Optional<PaymentEntity> findByPaymentId(String paymentId);

    Optional<PaymentEntity> findByTransactionId(String transactionId);

    List<PaymentEntity> findByCustomer(CustomerEntity customer);

    List<PaymentEntity> findByPaymentStatus(PaymentStatusEnum paymentStatus);

}
