package no.haugalandplus.val.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;


/**
 * Used for creating and validating the Jwt token.
 */
@Component
public class JwtTokenUtil {

    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final Long expireTime = 15 * 60 * 1000L;

    private TokenRevocationRepository tokenRevocationRepository;

    @Autowired
    public JwtTokenUtil(TokenRevocationRepository tokenRevocationRepository) {
        this.tokenRevocationRepository = tokenRevocationRepository;
    }

    public Claims isExpired(String token) {
        String t = token.substring(7);
        Claims claims = claimsFromJwt(t);
        if (tokenRevocationRepository.existsByJwtId(claims.getId())) {
            throw new BadCredentialsException("Authentication token is expired");
        }
        return claims;
    }

    private Claims claimsFromJwt(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BadCredentialsException("Authentication token is expired");
        } catch (Exception e) {
            throw new BadCredentialsException("Authentication token is not valid");
        }
    }

    public String createJWT(long userId) {
        Date now = new Date();
        Date expires = new Date(System.currentTimeMillis() + expireTime);

        return  "Bearer " + Jwts.builder()
                .subject(userId + "")
                .issuedAt(now)
                .expiration(expires)
                .id(UUID.randomUUID().toString())
                .signWith(secretKey)
                .compact();
    }

    public void expire() {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        TokenRevocation tr = new TokenRevocation(claims.getId(), new Date());
        tokenRevocationRepository.save(tr);
    }
}
