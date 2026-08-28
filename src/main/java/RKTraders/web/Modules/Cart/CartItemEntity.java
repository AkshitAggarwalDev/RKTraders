package RKTraders.web.Modules.Cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(exclude = {"cart", "product"})
@ToString(exclude = {"cart", "product"})
@RKTraders.web.Modules.Customer.Entity
public class CartItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "cart_id")
        @JsonIgnore
        private Entity cart;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private RKTraders.web.Modules.Product.Entity product;

        private int quantity;
    }
