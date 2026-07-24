package RKTraders.web.Service;

import RKTraders.web.DTO.OrderResponseDTO;
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
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        Cart cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }


// Product Status check  :
        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new RuntimeException(product.getName() + " is currently unavailable");
            }
            ;
        }

// Stock Validation :
        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            if (item.getQuantity() > product.getStock()) {
                throw new RuntimeException(
                        product.getName() + " has insufficient stock"
                );
            }
        }

// Total Calculation :
        double totalAmount = 0;

        for (CartItem item : cartItems) {

            totalAmount +=
                    item.getProduct().getPrice()
                            * item.getQuantity();

        }
// Creating and Saving Orders :

        Order order = new Order();

        order.setCustomer(customer);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepo.save(order);


// Creating and Saving OrderItems :
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(
                    product.getPrice() * item.getQuantity());

            orderItems.add(orderItem);

        }

        orderItemRepo.saveAll(orderItems);

        savedOrder.setOrderItems(orderItems);

        orderRepo.save(savedOrder);


        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            product.setStock(
                    product.getStock() - item.getQuantity()
            );

            productRepo.save(product);

        }

        cartItemRepo.deleteAll(cartItems);

    OrderResponseDTO response = new OrderResponseDTO();

    response.setMessage("Order Placed Successfully");
    response.setOrderId(savedOrder.getId());
    response.setTotalAmount(savedOrder.getTotalAmount());
    response.setOrderStatus(savedOrder.getOrderStatus());

    return savedOrder;
    }

    public List<Order> getMyOrders(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Order> orders = orderRepo.findByCustomerId(customer.getId());

        if (orders.isEmpty()) {
            throw new RuntimeException("No Orders Found");
        }

        return orders;
    }


    public Optional<Order> getOrderById(Integer id){
    return orderRepo.findById(id);

    }

    @Transactional
    public String cancelOrder(int orderId, String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found!"));

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

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
            throw new RuntimeException("No orders found!");
        }

        return orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = orderRepo.findByOrderStatus(status);

        if (orders.isEmpty()) {
            throw new RuntimeException("No orders found with status : " + status);
        }

        return orders;
    }

    public String updateOrderStatus(int orderId, OrderStatus status) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        order.setOrderStatus(status);

        orderRepo.save(order);

        return "Order status updated successfully.";
    }

    public long CountOrders(){
    return orderRepo.count();
    }
    public long countMyOrders(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

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

