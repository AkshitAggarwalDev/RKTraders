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
    private Repo customerRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder encoder;




    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<Entity> user = customerRepo.findByEmail(username);

        if(user.isEmpty()){
            throw new ResourceNotFoundException("User Not Found");
        }

        return new UserPrincipal(user.get());
    }




    public Entity registerUser(RegisterRequestDTO request){

        Entity user = new Entity();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setRole("CUSTOMER");
        user.setPassword(encoder.encode(request.getPassword()));

        return customerRepo.save(user);
    }




    public String loginUser(LoginRequestDTO login){

        Optional<Entity> user = customerRepo.findByEmail(login.getEmail());

        if(user.isEmpty()){
            return "Customer is not registered yet, Register yourself first ! ";
        }

        Entity existingUser = user.get();

        if(encoder.matches(login.getPassword(), existingUser.getPassword())){


            return jwtService.generateToken(existingUser.getEmail());
        }

        return "Invalid Password";
    }

    public Entity getProfile(String email){
        Optional<Entity> customer =
                customerRepo.findByEmail(email);

        if(customer.isEmpty()){
            throw new ResourceNotFoundException("You are not registered yet,Register yourself first ! ");
        }
        return customer.get();
    }

    public Entity updateProfile(String email, Entity updatedCustomer) {

        Optional<Entity> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            return null;
        }

        Entity existingCustomer = customer.get();

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());

        return customerRepo.save(existingCustomer);
    }

    public String updatePassword(String email,
                                 PasswordUpdateDTO request) {

        Optional<Entity> customer = customerRepo.findByEmail(email);

        if(customer.isEmpty()){
            return "Customer Not Found";
        }

        Entity existingCustomer = customer.get();

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

        Optional<Entity> customer = customerRepo.findByEmail(email);

        if (customer.isEmpty()) {
            return "Customer Not Found";
        }

        customerRepo.delete(customer.get());

        return "Profile Deleted Successfully";
    }






}