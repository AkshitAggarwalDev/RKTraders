package RKTraders.web.Modules.Payment;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Customer.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    PaymentRepo paymentRepo;

    public List<PaymentEntity> getMyPayments(String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return paymentRepo.findByCustomer(customer);
    }

    public PaymentEntity getPaymentById(String paymentId, String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        PaymentEntity payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized Payment");
        }

        return payment;
    }

    public List<PaymentEntity> getAllPayments() {

        return paymentRepo.findAll();

    }

    public List<PaymentEntity> getPaymentsByStatus(PaymentStatusEnum paymentStatus) {

        return paymentRepo.findByPaymentStatus(paymentStatus);

    }

    public Double getTotalRevenue() {

        List<PaymentEntity> payments =
                paymentRepo.findByPaymentStatus(PaymentStatusEnum.SUCCESS);

        double totalRevenue = payments.stream()
                .mapToDouble(PaymentEntity::getAmount)
                .sum();

        return totalRevenue;
    }
}
