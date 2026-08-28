package RKTraders.web.Modules.Cart;

import RKTraders.web.Modules.Customer.CustomerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@EqualsAndHashCode(exclude = {"customer", "cartItems"})
@ToString(exclude = {"customer", "cartItems"})
@jakarta.persistence.Entity

public class CartEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @OneToOne
        @JoinColumn(name = "customer_id")
        private CustomerEntity customer;

        @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<CartItemEntity> cartItems;

}
