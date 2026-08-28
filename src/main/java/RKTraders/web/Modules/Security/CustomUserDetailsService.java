package RKTraders.web.Modules.Security;

import RKTraders.web.Exceptions.ResourceNotFoundException;
import RKTraders.web.Modules.Customer.Entity;
import RKTraders.web.Modules.Customer.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private Repo customerRepo;

    @Autowired
    private RKTraders.web.Modules.Owner.Repo ownerRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<Entity> customer = customerRepo.findByEmail(username);

        if (customer.isPresent()) {
            return new UserPrincipal(customer.get());
        }

        Optional<RKTraders.web.Modules.Owner.Entity> owner = ownerRepo.findByOwnerEmail(username);

        if (owner.isPresent()) {
            return new UserPrincipal(owner.get());
        }

        throw new ResourceNotFoundException("User Not Found");
    }
}