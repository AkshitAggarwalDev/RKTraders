package RKTraders.web.Modules.Category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<RKTraders.web.Modules.Product.Entity> products;

}
