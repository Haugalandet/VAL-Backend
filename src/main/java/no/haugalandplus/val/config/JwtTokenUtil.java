package no.haugalandplus.val.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;


/**
 * Used for creating and validating the Jwt token.
 */
@Component
public class JwtTokenUtil {

    private final KeyPair secretKey = key();

    public Claims claimsFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(secretKey.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String createJWT(User user) {
        return  Jwts.builder()
                .subject(user.getUserId() +"")
                .signWith(secretKey.getPrivate())
                .compact();
    }

    // TODO get a proper key thing
    private KeyPair key() {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGenerator.initialize(2048); // You can specify the key size (e.g., 2048 bits)

        // Generate a key pair
        return keyPairGenerator.generateKeyPair();
    }
}
