package RKTraders.web.Modules.Customer;


import RKTraders.web.Modules.Cart.CartEntity;
import RKTraders.web.Modules.Review.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = "cart")
@ToString(exclude = "cart")
@jakarta.persistence.Entity
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

@JsonIgnore
    private String password;

    private String role;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ReviewEntity> reviews;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private CartEntity cart;

}