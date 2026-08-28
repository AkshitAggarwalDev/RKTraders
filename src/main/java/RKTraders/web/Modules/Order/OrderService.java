package RKTraders.web.Modules.Order;

import RKTraders.web.Modules.Cart.CartItemEntity;
import RKTraders.web.Modules.Cart.CartItemRepo;
import RKTraders.web.Exceptions.BadRequestException;
import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Owner.OrderStatus;
import RKTraders.web.Modules.Product.ProductStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {


    @Autowired
    RKTraders.web.Modules.Customer.Repo customerRepo;


    @Autowired
    RKTraders.web.Modules.Cart.Repo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Autowired
    Repo orderRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    RKTraders.web.Modules.Product.Repo productRepo;



    @Transactional
    public Entity placeOrder(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        RKTraders.web.Modules.Cart.Entity cart = cartRepo.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<CartItemEntity> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Product Status Validation
        for (CartItemEntity item : cartItems) {

            RKTraders.web.Modules.Product.Entity product = item.getProduct();

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new BadRequestException(product.getName() + " is currently unavailable");
            }
        }

        // Stock Validation
        for (CartItemEntity item : cartItems) {

            RKTraders.web.Modules.Product.Entity product = item.getProduct();

            if (item.getQuantity() > product.getStock()) {
                throw new BadRequestException(product.getName() + " has insufficient stock");
            }
        }

        // Calculate Total Amount
        double totalAmount = 0;

        for (CartItemEntity item : cartItems) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }

        // Create Order
        Entity order = new Entity();
        order.setCustomer(customer);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.PLACED);

        Entity savedOrder = orderRepo.save(order);

        // Create Order Items
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (CartItemEntity item : cartItems) {

            RKTraders.web.Modules.Product.Entity product = item.getProduct();

            OrderItemEntity orderItem = new OrderItemEntity();
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
        for (CartItemEntity item : cartItems) {

            RKTraders.web.Modules.Product.Entity product = item.getProduct();

            product.setStock(product.getStock() - item.getQuantity());

            productRepo.save(product);
        }

        // Clear Cart
        cartItemRepo.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Entity> getMyOrders(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Entity> orders = orderRepo.findByCustomerId(customer.getId());

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No Orders Found");
        }

        return orders;
    }


    public Optional<Entity> getOrderById(Integer id){
    return orderRepo.findById(id);

    }

    @Transactional
    public String cancelOrder(int orderId, String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found!"));

        Entity order = orderRepo.findById(orderId)
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
        for (OrderItemEntity item : order.getOrderItems()) {

            RKTraders.web.Modules.Product.Entity product = item.getProduct();

            product.setStock(
                    product.getStock() + item.getQuantity()
            );

            productRepo.save(product);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        orderRepo.save(order);

        return "Order cancelled successfully.";
    }

    public List<Entity> getAllOrders() {

        List<Entity> orders = orderRepo.findAll();

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found!");
        }

        return orderRepo.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    public List<Entity> getOrdersByStatus(OrderStatus status) {

        List<Entity> orders = orderRepo.findByOrderStatus(status);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found with status : " + status);
        }

        return orders;
    }

    public String updateOrderStatus(int orderId, OrderStatus status) {

        Entity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));

        order.setOrderStatus(status);

        orderRepo.save(order);

        return "Order status updated successfully.";
    }

    public long CountOrders(){
    return orderRepo.count();
    }
    public long countMyOrders(String email) {

        RKTraders.web.Modules.Customer.Entity customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return orderRepo.countByCustomer(customer);

    }

    public Double getTotalRevenue() {

        List<Entity> orders = orderRepo.findAll();

        double totalRevenue = 0;

        for (Entity order : orders) {

            if (order.getOrderStatus() == OrderStatus.PLACED) {

                totalRevenue = totalRevenue + order.getTotalAmount();

            }

        }

        return totalRevenue;
    }

    public List<Entity> getRecentOrders() {

        return orderRepo.findTop10ByOrderByOrderDateDesc();

    }
    public List<Entity> getTodayOrders() {

        List<Entity> orders = orderRepo.findAll();

        List<Entity> todayOrders = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Entity order : orders) {

            if (order.getOrderDate().toLocalDate().equals(today)) {

                todayOrders.add(order);

            }

        }

        return todayOrders;

    }

    public List<Entity> getOrdersBetweenDates(LocalDate startDate,
                                              LocalDate endDate) {

        List<Entity> orders = orderRepo.findAll();

        List<Entity> filteredOrders = new ArrayList<>();

        for (Entity order : orders) {

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

