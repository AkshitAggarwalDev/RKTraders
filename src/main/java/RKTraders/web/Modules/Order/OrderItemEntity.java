package RKTraders.web.Modules.Order;
import jakarta.persistence.*;
import lombok.*;

    @RKTraders.web.Modules.Customer.Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrderItemEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Entity order;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private RKTraders.web.Modules.Product.Entity product;

        private String productName;

        private double productPrice;

        private int quantity;

        private double totalPrice;
    }
