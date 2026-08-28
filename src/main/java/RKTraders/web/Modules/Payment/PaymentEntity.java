package RKTraders.web.Modules.Payment;
import RKTraders.web.Modules.Address.AddressEntity;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Order.OrderEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

    @jakarta.persistence.Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PaymentEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(unique = true, nullable = false)
        private String paymentId = UUID.randomUUID().toString();

        private Double amount;

        @Enumerated(EnumType.STRING)
        private PaymentMethodEnum paymentMethod;

        @Enumerated(EnumType.STRING)
        private PaymentStatusEnum paymentStatus;

        private String upiId;

        @Column(unique = true)
        private String transactionId;

        private LocalDateTime paidAt;

        @ManyToOne
        @JoinColumn(name = "customer_id")
        @JsonIgnore
        private CustomerEntity customer;

        @OneToOne
        @JoinColumn(name = "order_id")
        @JsonIgnore
        private OrderEntity order;

        @ManyToOne
        @JoinColumn(name = "address_id")
        @JsonIgnore
        private AddressEntity address;
    }
