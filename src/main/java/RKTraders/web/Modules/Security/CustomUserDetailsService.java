package RKTraders.web.Modules.Security;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Customer.CustomerEntity;
import RKTraders.web.Modules.Customer.CustomerRepo;
import RKTraders.web.Modules.Owner.OwnerEntity;
import RKTraders.web.Modules.Owner.OwnerRepo;
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

        Optional<CustomerEntity> customer = customerRepo.findByEmail(username);

        if (customer.isPresent()) {
            return new UserPrincipal(customer.get());
        }

        Optional<OwnerEntity> owner = ownerRepo.findByOwnerEmail(username);

        if (owner.isPresent()) {
            return new UserPrincipal(owner.get());
        }

        throw new ResourceNotFoundException("User Not Found");
    }
}