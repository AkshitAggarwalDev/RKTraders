    package RKTraders.web.Modules.Security;

    import RKTraders.web.Modules.Admin.Entity;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    public class UserPrincipal implements UserDetails {

        private String email;
        private String password;
        private String role;

        public UserPrincipal(RKTraders.web.Modules.Customer.Entity customer) {
            this.email = customer.getEmail();
            this.password = customer.getPassword();
            this.role = customer.getRole();
        }

        public UserPrincipal(Entity admin) {
            this.password = admin.getAdminPassword();
            this.role = admin.getRole();
        }

        public UserPrincipal(RKTraders.web.Modules.Owner.Entity owner) {
            this.email = owner.getOwnerEmail();
            this.password = owner.getOwnerPassword();
            this.role = owner.getRole();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }