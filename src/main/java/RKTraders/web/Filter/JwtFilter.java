package RKTraders.web.Filter;

import RKTraders.web.Service.CustomUserDetailsService;
import RKTraders.web.Service.JwtService;
import RKTraders.web.Service.CustomerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("================================");
        System.out.println(request.getRequestURI());

        String authHeader = request.getHeader("Authorization");

        System.out.println(authHeader);
        String token = null;
        String userName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            userName = jwtService.extractUserName(token);
            System.out.println(userName);
        }

        if (userName != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    context.getBean(CustomUserDetailsService.class)
                            .loadUserByUsername(userName);

            if (jwtService.validateToken(token, userDetails)) {
                System.out.println("Valid Token");
                System.out.println("Authorities : " + userDetails.getAuthorities());
            } else {
                System.out.println("Invalid Token");
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        }
        filterChain.doFilter(request,response);
        System.out.println("Reached Here ! ");
    }

//        filterChain.doFilter(request,response);
}