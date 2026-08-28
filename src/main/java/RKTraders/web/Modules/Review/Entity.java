package RKTraders.web.Modules.Review;
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
    public class Entity {

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
        private RKTraders.web.Modules.Customer.Entity customer;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private RKTraders.web.Modules.Product.Entity product;

        private Boolean verifiedPurchase = false;
    }
