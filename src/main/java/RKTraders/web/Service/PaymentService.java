package RKTraders.web.Service;

import RKTraders.web.DTO.OrderResponseDTO;
import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ForbiddenException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import RKTraders.web.Model.*;
import RKTraders.web.Repositories.*;
import RKTraders.web.enums.PaymentMethod;
import RKTraders.web.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public Payment initiatePayment(Integer addressId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new ForbiddenException("This address does not belong to the logged-in customer");
        }

        Cart cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Payment payment = new Payment();

        payment.setAmount(totalAmount);
        payment.setPaymentMethod(PaymentMethod.UPI);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setUpiId("rktraders@okaxis");

        payment.setCustomer(customer);
        payment.setAddress(address);

        return paymentRepo.save(payment);
    }
@Autowired
CartService cartService;
    @Autowired
    OrderService orderService;
    public Payment verifyPayment(String paymentId, String transactionId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Payment payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized Payment");
        }

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment already verified");
        }

        if (paymentRepo.findByTransactionId(transactionId).isPresent()) {
            throw new BadRequestException("Transaction ID already exists");
        }

        payment.setTransactionId(transactionId);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepo.save(payment);

        Order order = orderService.placeOrder(email);

        payment.setOrder(order);

        return paymentRepo.save(payment);
    }

    public List<Payment> getMyPayments(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return paymentRepo.findByCustomer(customer);
    }

    public Payment getPaymentById(String paymentId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Payment payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized Payment");
        }

        return payment;
    }

    public List<Payment> getAllPayments() {

        return paymentRepo.findAll();

    }

    public List<Payment> getPaymentsByStatus(PaymentStatus paymentStatus) {

        return paymentRepo.findByPaymentStatus(paymentStatus);

    }

    public Double getTotalRevenue() {

        List<Payment> payments =
                paymentRepo.findByPaymentStatus(PaymentStatus.SUCCESS);

        double totalRevenue = payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        return totalRevenue;
    }
}
