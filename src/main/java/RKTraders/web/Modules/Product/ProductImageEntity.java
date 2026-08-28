package RKTraders.web.Modules.Product;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RKTraders.web.Modules.Customer.Entity
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageUrl;

    private boolean primaryImage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Entity product;

}
