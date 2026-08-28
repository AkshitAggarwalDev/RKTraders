package RKTraders.web.Modules.Payment;

import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ForbiddenException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import RKTraders.web.Modules.Address.AddressEntity;
import RKTraders.web.Modules.Address.AddressRepo;
import RKTraders.web.Modules.Cart.*;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Customer.CustomerRepo;
import RKTraders.web.Modules.Order.OrderEntity;
import RKTraders.web.Modules.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    CartRepo cartRepo;
    @Autowired
    PaymentRepo paymentRepo;
    @Autowired
    CartItemRepo cartItemRepo;

    public PaymentEntity initiatePayment(Integer addressId, String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        AddressEntity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new ForbiddenException("This address does not belong to the logged-in customer");
        }

        CartEntity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        PaymentEntity payment = new PaymentEntity();

        payment.setAmount(totalAmount);
        payment.setPaymentMethod(PaymentMethodEnum.UPI);
        payment.setPaymentStatus(PaymentStatusEnum.PENDING);
        payment.setUpiId("rktraders@okaxis");

        payment.setCustomer(customer);
        payment.setAddress(address);

        return paymentRepo.save(payment);
    }
@Autowired
CartService cartService;
    @Autowired
    OrderService orderService;
    public PaymentEntity verifyPayment(String paymentId, String transactionId, String email) {

        CustomerEntity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        PaymentEntity payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized Payment");
        }

        if (payment.getPaymentStatus() == PaymentStatusEnum.SUCCESS) {
            throw new BadRequestException("Payment already verified");
        }

        if (paymentRepo.findByTransactionId(transactionId).isPresent()) {
            throw new BadRequestException("Transaction ID already exists");
        }

        payment.setTransactionId(transactionId);
        payment.setPaymentStatus(PaymentStatusEnum.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepo.save(payment);

        OrderEntity order = orderService.placeOrder(email);

        payment.setOrder(order);

        return paymentRepo.save(payment);
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
