package RKTraders.web.Modules.Enquiry;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RKTraders.web.Modules.Customer.Entity
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String customerName;

    private String email;

    private String phone;

    private String message;

    @ManyToOne
    private RKTraders.web.Modules.Product.Entity product;

}