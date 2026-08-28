package RKTraders.web.Modules.Product;

import RKTraders.web.Modules.Cart.CartItemEntity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"category", "cartItems", "images", "reviews"})
@ToString(exclude = {"category", "cartItems", "images", "reviews"})
@jakarta.persistence.Entity
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private double price;

    private int stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductImageEntity> images;

    private String brand;

    private String color;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CartItemEntity> cartItems;

    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private RKTraders.web.Modules.Category.Entity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RKTraders.web.Modules.Review.Entity> reviews;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}