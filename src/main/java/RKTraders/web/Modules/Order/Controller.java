package RKTraders.web.Modules.Order;

import RKTraders.web.Modules.Owner.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class Controller {


    @Autowired
    OrderService orderService;

    @PostMapping("place")
    public Entity placeOrder(Authentication authentication){

        return orderService.placeOrder(authentication.getName());

    }

    @GetMapping("myOrders")
    public ResponseEntity<List<Entity>> getMyOrders(Authentication authentication) {

        return ResponseEntity.ok(
                orderService.getMyOrders(authentication.getName())
        );

    }

    @GetMapping("getOrderById")
    public Optional<Entity> GetOrdersById(Integer id){
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
    public ResponseEntity<List<Entity>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Entity>> getOrdersByStatus(
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
    public ResponseEntity<List<Entity>> getRecentOrders() {

        return ResponseEntity.ok(orderService.getRecentOrders());

    }

    @GetMapping("today")
    public ResponseEntity<List<Entity>> getTodayOrders() {

        return ResponseEntity.ok(orderService.getTodayOrders());

    }

    @GetMapping("between-dates")
    public ResponseEntity<List<Entity>> getOrdersBetweenDates(

            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                orderService.getOrdersBetweenDates(startDate, endDate)
        );

    }
}
