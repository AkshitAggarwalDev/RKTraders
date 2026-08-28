package RKTraders.web.Modules.Review;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

    @jakarta.persistence.Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReviewEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(nullable = false)
        private Integer rating;

        private String comment;

        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "customer_id", nullable = false)
        private CustomerEntity customer;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private ProductEntity product;

        private Boolean verifiedPurchase = false;
    }
