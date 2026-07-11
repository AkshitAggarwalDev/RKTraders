package RKTraders.web.Controller;

import RKTraders.web.DTO.LoginRequestDTO;
import RKTraders.web.DTO.RegisterRequestDTO;
import RKTraders.web.Model.Customer;
import RKTraders.web.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class AuthorizationController {

    @Autowired
    CustomerService customerService;

    @PostMapping("register")
    public Customer register(@RequestBody RegisterRequestDTO request) {
        return customerService.registerUser(request);
    }

    @PostMapping("login")
    public String userLogin(@RequestBody LoginRequestDTO login){

        return customerService.loginUser(login);

    }
}
