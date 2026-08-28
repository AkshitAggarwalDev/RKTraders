package RKTraders.web.Modules.Customer;

import RKTraders.web.Modules.Owner.OrderStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
           private int OrderId;

    private String  message;

            private OrderStatus OrderStatus;

           private double totalAmount;

            private LocalDateTime orderDate;
}
