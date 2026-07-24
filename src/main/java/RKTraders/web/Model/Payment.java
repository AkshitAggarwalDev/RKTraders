package RKTraders.web.Model;
import RKTraders.web.enums.PaymentMethod;
import RKTraders.web.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(unique = true, nullable = false)
        private String paymentId = UUID.randomUUID().toString();

        private Double amount;

        @Enumerated(EnumType.STRING)
        private PaymentMethod paymentMethod;

        @Enumerated(EnumType.STRING)
        private PaymentStatus paymentStatus;

        private String upiId;

        @Column(unique = true)
        private String transactionId;

        private LocalDateTime paidAt;

        @ManyToOne
        @JoinColumn(name = "customer_id")
        @JsonIgnore
        private Customer customer;

        @OneToOne
        @JoinColumn(name = "order_id")
        @JsonIgnore
        private Order order;

        @ManyToOne
        @JoinColumn(name = "address_id")
        @JsonIgnore
        private Address address;
    }
