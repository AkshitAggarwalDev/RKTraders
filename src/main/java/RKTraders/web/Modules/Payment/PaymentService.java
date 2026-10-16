package RKTraders.web.Modules.Payment;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import RKTraders.web.Modules.Address.AddressEntity;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Customer.CustomerRepo;
import RKTraders.web.Modules.Order.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    PaymentRepo paymentRepo;

    public PaymentEntity createPendingPayment(OrderEntity order, CustomerEntity customer, AddressEntity address) {
        PaymentEntity payment = new PaymentEntity();
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatusEnum.PENDING);
        payment.setPaymentMethod(PaymentMethodEnum.CASH_ON_DELIVERY);
        payment.setCustomer(customer);
        payment.setOrder(order);
        payment.setAddress(address);
        PaymentEntity saved = paymentRepo.save(payment);
        // Keep the transient payment status on the order in sync with the
        // payment record so the place-order response carries it immediately.
        order.setPaymentStatus(saved.getPaymentStatus());
        return saved;
    }

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

    public PaymentEntity getMyPaymentForOrder(int orderId, String email) {
        return requireOwnedOrderPayment(orderId, email);
    }

    private PaymentEntity requireOwnedOrderPayment(int orderId, String email) {
        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        PaymentEntity payment = paymentRepo.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        if (payment.getCustomer().getId() != customer.getId()) throw new UnauthorizedException("Unauthorized Payment");
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
