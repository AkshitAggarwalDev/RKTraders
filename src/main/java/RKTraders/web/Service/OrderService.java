package RKTraders.web.Service;

import RKTraders.web.DTO.OrderResponseDTO;
import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Model.*;
import RKTraders.web.Repositories.*;
import RKTraders.web.enums.OrderStatus;
import RKTraders.web.enums.ProductStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {


    @Autowired
    CustomerRepo customerRepo;


    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    ProductRepo productRepo;



    @Transactional
    public Order placeOrder(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Cart cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Product Status Validation
        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new BadRequestException(product.getName() + " is currently unavailable");
            }
        }

        // Stock Validation
        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (item.getQuantity() > product.getStock()) {
                throw new BadRequestException(product.getName() + " has insufficient stock");
            }
        }

        // Calculate Total Amount
        double totalAmount = 0;

        for (CartItem item : cartItems) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        // Create Order
        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepo.save(order);

        // Create Order Items
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(product.getPrice() * item.getQuantity());

            orderItems.add(orderItem);
        }

        orderItemRepo.saveAll(orderItems);

        savedOrder.setOrderItems(orderItems);

        // Update Product Stock
        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            product.setStock(product.getStock() - item.getQuantity());

            productRepo.save(product);
        }

        // Clear Cart
        cartItemRepo.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Order> getMyOrders(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Order> orders = orderRepo.findByCustomerId(customer.getId());

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No Orders Found");
        }

        return orders;
    }


    public Optional<Order> getOrderById(Integer id){
    return orderRepo.findById(id);

    }

    @Transactional
    public String cancelOrder(int orderId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found!"));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        // Check ownership
        if (order.getCustomer().getId() != customer.getId()) {
            return "You are not authorized to cancel this order.";
        }

        // Already cancelled
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            return "Order is already cancelled.";
        }

        // Delivered orders cannot be cancelled
        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            return "Delivered orders cannot be cancelled.";
        }

        // Restore stock
        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setStock(
                    product.getStock() + item.getQuantity()
            );

            productRepo.save(product);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepo.save(order);

        return "Order cancelled successfully.";
    }

    public List<Order> getAllOrders() {

        List<Order> orders = orderRepo.findAll();

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found!");
        }

        return orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = orderRepo.findByOrderStatus(status);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found with status : " + status);
        }

        return orders;
    }

    public String updateOrderStatus(int orderId, OrderStatus status) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        order.setOrderStatus(status);

        orderRepo.save(order);

        return "Order status updated successfully.";
    }

    public long CountOrders(){
    return orderRepo.count();
    }
    public long countMyOrders(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return orderRepo.countByCustomer(customer);

    }

    public Double getTotalRevenue() {

        List<Order> orders = orderRepo.findAll();

        double totalRevenue = 0;

        for (Order order : orders) {

            if (order.getOrderStatus() == OrderStatus.PLACED) {

                totalRevenue = totalRevenue + order.getTotalAmount();

            }

        }

        return totalRevenue;
    }

    public List<Order> getRecentOrders() {

        return orderRepo.findTop10ByOrderByOrderDateDesc();

    }
    public List<Order> getTodayOrders() {

        List<Order> orders = orderRepo.findAll();

        List<Order> todayOrders = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Order order : orders) {

            if (order.getOrderDate().toLocalDate().equals(today)) {

                todayOrders.add(order);

            }

        }

        return todayOrders;

    }

    public List<Order> getOrdersBetweenDates(LocalDate startDate,
                                             LocalDate endDate) {

        List<Order> orders = orderRepo.findAll();

        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {

            LocalDate orderDate = order.getOrderDate().toLocalDate();

            if ((orderDate.isEqual(startDate) || orderDate.isAfter(startDate))
                    &&
                    (orderDate.isEqual(endDate) || orderDate.isBefore(endDate))) {

                filteredOrders.add(order);

            }

        }

        return filteredOrders;

    }
}

