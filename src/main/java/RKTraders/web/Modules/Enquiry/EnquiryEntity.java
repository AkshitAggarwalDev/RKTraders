package RKTraders.web.Modules.Enquiry;

import RKTraders.web.Modules.Product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class EnquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String customerName;

    private String email;

    private String phone;

    private String message;

    @ManyToOne
    private ProductEntity product;
}