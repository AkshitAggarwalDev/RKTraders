package RKTraders.web.DTO;

import lombok.Data;

@Data
public class PasswordUpdateDTO {

    private String oldPassword;
    private String newPassword;

}