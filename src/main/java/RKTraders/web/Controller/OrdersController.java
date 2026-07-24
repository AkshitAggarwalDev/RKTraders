package RKTraders.web.Controller;

import RKTraders.web.Model.Order;
import RKTraders.web.Service.OrderService;
import RKTraders.web.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrdersController {


    @Autowired
    OrderService orderService;

    @PostMapping("place")
    public Order placeOrder(Authentication authentication){

        return orderService.placeOrder(authentication.getName());

    }

    @GetMapping("myOrders")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrders(authentication.getName())
        );

    }

    @GetMapping("getOrderById")
    public Optional<Order> GetOrdersById(Integer id){
        return orderService.getOrderById(id);
    }


    @DeleteMapping("cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(
            @PathVariable int orderId,
            Authentication authentication) {

        String response = orderService.cancelOrder(
                orderId,
                authentication.getName()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("all")
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @PathVariable OrderStatus status) {

        return ResponseEntity.ok(
                orderService.getOrdersByStatus(status)
        );
    }

    @PutMapping("status/{orderId}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable int orderId,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, status)
        );
    }
    @GetMapping("count")
    public long countOrders(){
        return orderService.CountOrders();
    }

    @GetMapping("my/count")
    public ResponseEntity<Long> countMyOrders(
            Authentication authentication) {

        return ResponseEntity.ok(

                orderService.countMyOrders(
                        authentication.getName()
                )

        );

    }

    @GetMapping("revenue")
    public ResponseEntity<Double> getTotalRevenue() {

        return ResponseEntity.ok(orderService.getTotalRevenue());

    }

    @GetMapping("recent")
    public ResponseEntity<List<Order>> getRecentOrders() {

        return ResponseEntity.ok(orderService.getRecentOrders());

    }

    @GetMapping("today")
    public ResponseEntity<List<Order>> getTodayOrders() {

        return ResponseEntity.ok(orderService.getTodayOrders());

    }

    @GetMapping("between-dates")
    public ResponseEntity<List<Order>> getOrdersBetweenDates(

            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                orderService.getOrdersBetweenDates(startDate, endDate)
        );

    }
}
