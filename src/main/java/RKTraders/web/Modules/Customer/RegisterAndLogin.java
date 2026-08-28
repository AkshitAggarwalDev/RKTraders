package RKTraders.web.Modules.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class RegisterAndLogin {

    @Autowired
    CustomerService customerService;

    @PostMapping("register")
    public CustomerEntity register(@RequestBody RegisterRequestDTO request) {
        return customerService.registerUser(request);
    }

    @PostMapping("login")
    public String userLogin(@RequestBody LoginRequestDTO login){

        return customerService.loginUser(login);

    }
}
