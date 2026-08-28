package RKTraders.web.Modules.Customer;

import RKTraders.web.Modules.Security.JwtService;
import RKTraders.web.Modules.Security.UserPrincipal;
import RKTraders.web.Exceptions.ResourceNotFoundException;

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

        Optional<CustomerEntity> user = customerRepo.findByEmail(username);

        if(user.isEmpty()){
            throw new ResourceNotFoundException("User Not Found");
        }

        return new UserPrincipal(user.get());
    }




    public CustomerEntity registerUser(RegisterRequestDTO request){

        CustomerEntity user = new CustomerEntity();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setRole("CUSTOMER");
        user.setPassword(encoder.encode(request.getPassword()));

        return customerRepo.save(user);
    }




    public String loginUser(LoginRequestDTO login){

        Optional<CustomerEntity> user = customerRepo.findByEmail(login.getEmail());

        if(user.isEmpty()){
            return "Customer is not registered yet, Register yourself first ! ";
        }

        CustomerEntity existingUser = user.get();

        if(encoder.matches(login.getPassword(), existingUser.getPassword())){


            return jwtService.generateToken(existingUser.getEmail());
        }

        return "Invalid Password";
    }

    public CustomerEntity getProfile(String email){
        Optional<CustomerEntity> customer =
                customerRepo.findByEmail(email);

        if(customer.isEmpty()){
            throw new ResourceNotFoundException("You are not registered yet,Register yourself first ! ");
        }
        return customer.get();
    }

    public CustomerEntity updateProfile(String email, CustomerEntity updatedCustomer) {

        Optional<CustomerEntity> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            return null;
        }

        CustomerEntity existingCustomer = customer.get();

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        return customerRepo.save(existingCustomer);
    }

    public String updatePassword(String email,
                                 PasswordUpdateDTO request) {

        Optional<CustomerEntity> customer = customerRepo.findByEmail(email);

        if(customer.isEmpty()){
            return "Customer Not Found";
        }

        CustomerEntity existingCustomer = customer.get();

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

        Optional<CustomerEntity> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            return "Customer Not Found";
        }

        customerRepo.delete(customer.get());

        return "Profile Deleted Successfully";
    }






}