package RKTraders.web.Modules.Cart;

import RKTraders.web.Modules.Product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(exclude = {"cart", "product"})
@ToString(exclude = {"cart", "product"})
@jakarta.persistence.Entity
public class CartItemEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "cart_id")
        @JsonIgnore
        private CartEntity cart;

        @ManyToOne
        @JoinColumn(name = "product_id")
        private ProductEntity product;

        private int quantity;
}