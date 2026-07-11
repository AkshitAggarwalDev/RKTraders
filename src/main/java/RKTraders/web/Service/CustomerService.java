package RKTraders.web.Service;

import RKTraders.web.DTO.LoginRequestDTO;
import RKTraders.web.DTO.PasswordUpdateDTO;
import RKTraders.web.DTO.RegisterRequestDTO;
import RKTraders.web.Model.Customer;
import RKTraders.web.Repositories.CustomerRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder encoder;




    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<Customer> user = customerRepo.findByEmail(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User Not Found");
        }

        return new UserPrincipal(user.get());
    }




    public Customer registerUser(RegisterRequestDTO request){

        Customer user = new Customer();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setRole("CUSTOMER");
        user.setPassword(encoder.encode(request.getPassword()));

        return customerRepo.save(user);
    }




    public String loginUser(LoginRequestDTO login){

        Optional<Customer> user = customerRepo.findByEmail(login.getEmail());

        if(user.isEmpty()){
            return "Customer is not registered yet, Register yourself first ! ";
        }

        Customer existingUser = user.get();

        if(encoder.matches(login.getPassword(), existingUser.getPassword())){


            return jwtService.generateToken(existingUser.getEmail());
        }

        return "Invalid Password";
    }

    public Customer getProfile(String email){
        Optional<Customer> customer =
                customerRepo.findByEmail(email);

        if(customer.isEmpty()){
            throw new UsernameNotFoundException("You are not registered yet,Register yourself first ! ");
        }
        return customer.get();
    }

    public Customer updateProfile(String email, Customer updatedCustomer) {

        Optional<Customer> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            return null;
        }

        Customer existingCustomer = customer.get();

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        return customerRepo.save(existingCustomer);
    }

    public String updatePassword(String email,
                                 PasswordUpdateDTO request) {

        Optional<Customer> customer = customerRepo.findByEmail(email);

        if(customer.isEmpty()){
            return "Customer Not Found";
        }

        Customer existingCustomer = customer.get();

        if(!encoder.matches(request.getOldPassword(),
                existingCustomer.getPassword())){

            return "Old Password is Incorrect";
        }

        existingCustomer.setPassword(
                encoder.encode(request.getNewPassword())
        );

        customerRepo.save(existingCustomer);

        return "Password Updated Successfully";
    }

    public String updateMobileNo(){
        return null;
    }

    public String deleteProfile(String email) {

        Optional<Customer> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            return "Customer Not Found";
        }

        customerRepo.delete(customer.get());

        return "Profile Deleted Successfully";
    }






}