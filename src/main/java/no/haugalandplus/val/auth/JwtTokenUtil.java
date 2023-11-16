package no.haugalandplus.val.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;


/**
 * Used for creating and validating the Jwt token. Also manages
 * the token blacklist (revoked tokens)
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secretKey}")
    private String secretKey = "Dette er min tipp topp hemmelige nøkkelt ting!";
    private final Long expireTime = 15 * 60 * 1000L;
    private final TokenRevocationRepository tokenRevocationRepository;

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
                    .verifyWith(getSecretKey())
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
                .signWith(getSecretKey())
                .compact();
    }

    public void expire() {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        TokenRevocation tr = new TokenRevocation(claims.getId(), new Date());
        tokenRevocationRepository.save(tr);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void deleteExpired() {
        tokenRevocationRepository.deleteAllByExpirationTimeBefore(new Date(System.currentTimeMillis() - expireTime));
    }
}
