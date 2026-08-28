package RKTraders.web.Modules.Customer;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = "cart")
@ToString(exclude = "cart")
@jakarta.persistence.Entity
public class Entity {

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
    private List<RKTraders.web.Modules.Review.Entity> reviews;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private RKTraders.web.Modules.Cart.Entity cart;

}