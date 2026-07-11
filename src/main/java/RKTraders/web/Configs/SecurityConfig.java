package RKTraders.web.Configs;

import RKTraders.web.Filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/customer/register",
                                "/customer/login",
                                "/owner/settings/login"
                        ).permitAll()

                        .requestMatchers("/owner/**")
                        .hasRole("OWNER")

                        .requestMatchers("/admin/**")
                        .hasAnyRole("ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.GET, "/products/**")
                        .hasAnyRole("CUSTOMER", "ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.POST, "/products/**")
                        .hasAnyRole("ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.PUT, "/products/**")
                        .hasAnyRole("ADMIN", "OWNER")

                        .requestMatchers(HttpMethod.DELETE, "/products/**")
                        .hasAnyRole("ADMIN", "OWNER")

                        .requestMatchers("/customer/**")
                        .hasAnyRole("CUSTOMER", "OWNER")

                        .requestMatchers("/category/**").permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}