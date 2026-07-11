package RKTraders.web.Service;

import RKTraders.web.Model.Customer;
import RKTraders.web.Model.Owner;
import RKTraders.web.Repositories.CustomerRepo;
import RKTraders.web.Repositories.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OwnerRepo ownerRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<Customer> customer = customerRepo.findByEmail(username);

        if (customer.isPresent()) {
            return new UserPrincipal(customer.get());
        }

        Optional<Owner> owner = ownerRepo.findByOwnerEmail(username);

        if (owner.isPresent()) {
            return new UserPrincipal(owner.get());
        }

        throw new UsernameNotFoundException("User Not Found");
    }
}