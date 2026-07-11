package RKTraders.web.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;




    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getKeys(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKeys() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    //============================
    // Extract Username
    //============================

    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    //============================
    // Generic Claim Extractor
    //============================

    public <T> T extractClaim(String token,
                              Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    //============================
    // Extract All Claims
    //============================

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getKeys())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    //============================
    // Expiry Check
    //============================

    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration)
                .before(new Date());

    }

    //============================
    // Validate Token
    //============================

    public boolean validateToken(String token,
                                 UserDetails userDetails) {

        final String username = extractUserName(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);

    }

}