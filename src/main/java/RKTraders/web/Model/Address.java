package RKTraders.web.Model;

import RKTraders.web.Model.Customer;
import RKTraders.web.enums.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String houseNumber;

    private String street;

    private String NearByLoc;

    private String city;

    private String state;

    private String pincode;

    private String country;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private Boolean defaultAddress = false;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
}