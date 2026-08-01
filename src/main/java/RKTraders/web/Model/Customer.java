package RKTraders.web.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = "cart")
@ToString(exclude = "cart")
@Entity
public class Customer{

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
    private List<Review> reviews;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

}