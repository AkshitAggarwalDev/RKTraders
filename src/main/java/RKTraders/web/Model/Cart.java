package RKTraders.web.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@EqualsAndHashCode(exclude = {"customer", "cartItems"})
@ToString(exclude = {"customer", "cartItems"})
@Entity

public class Cart {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @OneToOne
        @JoinColumn(name = "customer_id")
        private Customer customer;

        @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<CartItem> cartItems;

}
