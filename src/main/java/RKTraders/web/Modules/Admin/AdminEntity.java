package RKTraders.web.Modules.Admin;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@jakarta.persistence.Entity
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String adminName;

    public String adminAge;

    public String adminSalary;

    public String adminResidence;

    public String adminPassword;

    private String role;
}
