package RKTraders.web.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(exclude = {"cart", "product"})
@ToString(exclude = {"cart", "product"})
@Entity
public class CartItem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "cart_id")
        @JsonIgnore
        private Cart cart;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private Product product;

        private int quantity;
    }
