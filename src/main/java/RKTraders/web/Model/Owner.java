package RKTraders.web.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ownerName;

    @Column(unique = true)
    private String ownerEmail;

    private String ownerPassword;

    @Column(unique = true)
    private String ownerMobileNo;

    private String companyName;

    private String companyAddress;

    private String gstNumber;

    private String role;

}