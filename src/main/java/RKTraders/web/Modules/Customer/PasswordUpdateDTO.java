package RKTraders.web.Modules.Customer;

import lombok.Data;

@Data
public class PasswordUpdateDTO {

    private String oldPassword;
    private String newPassword;

}