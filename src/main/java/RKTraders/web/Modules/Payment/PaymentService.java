package RKTraders.web.Modules.Payment;

import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ForbiddenException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Exceptions.UnauthorizedException;
import RKTraders.web.Modules.Cart.*;
import RKTraders.web.Modules.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    RKTraders.web.Modules.Customer.Repo customerRepo;
    @Autowired
    RKTraders.web.Modules.Address.Repo addressRepo;
    @Autowired
    RKTraders.web.Modules.Cart.Repo cartRepo;
    @Autowired
    Repo paymentRepo;
    @Autowired
    CartItemRepo cartItemRepo;

    public Entity initiatePayment(Integer addressId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        RKTraders.web.Modules.Address.Entity address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (address.getCustomer().getId() != customer.getId()) {
            throw new ForbiddenException("This address does not belong to the logged-in customer");
        }

        RKTraders.web.Modules.Cart.Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Entity payment = new Entity();

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
    public Entity verifyPayment(String paymentId, String transactionId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity payment = paymentRepo.findByPaymentId(paymentId)
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

        RKTraders.web.Modules.Order.Entity order = orderService.placeOrder(email);

        payment.setOrder(order);

        return paymentRepo.save(payment);
    }

    public List<Entity> getMyPayments(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return paymentRepo.findByCustomer(customer);
    }

    public Entity getPaymentById(String paymentId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Entity payment = paymentRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getCustomer().getId() != customer.getId()) {
            throw new UnauthorizedException("Unauthorized Payment");
        }

        return payment;
    }

    public List<Entity> getAllPayments() {

        return paymentRepo.findAll();

    }

    public List<Entity> getPaymentsByStatus(PaymentStatusEnum paymentStatus) {

        return paymentRepo.findByPaymentStatus(paymentStatus);

    }

    public Double getTotalRevenue() {

        List<Entity> payments =
                paymentRepo.findByPaymentStatus(PaymentStatusEnum.SUCCESS);

        double totalRevenue = payments.stream()
                .mapToDouble(Entity::getAmount)
                .sum();

        return totalRevenue;
    }
}
