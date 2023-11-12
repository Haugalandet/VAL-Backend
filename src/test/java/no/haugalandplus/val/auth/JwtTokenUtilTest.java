package no.haugalandplus.val.auth;

import io.jsonwebtoken.Claims;
import no.haugalandplus.val.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenUtilTest {

    @Autowired
    private TokenRevocationRepository tokenRevocationRepository;
    private String testName;
    private long userId;
    private User user;
    private JwtTokenUtil jwt;

    @BeforeEach
    public void init() {
        testName = "Ole Jonny";
        userId = 34;

        user = new User();
        user.setUsername(testName);
        user.setUserId(userId);

        jwt = new JwtTokenUtil(tokenRevocationRepository);
    }

    @Test
    @Transactional
    public void testValidation() {
        String token = jwt.createJWT(user.getUserId());

        Claims claims = jwt.isExpired(token);

        assertThat(claims.getSubject(), is(userId + ""));
    }

    @Test
    @Transactional
    public void testExpired() {
        // Change the time expireTime, so that the token is already expired.
        ReflectionTestUtils.setField(jwt, "expireTime", -1000L);

        String token = jwt.createJWT(user.getUserId());

        try {
            jwt.isExpired(token);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(BadCredentialsException.class));
        }
    }

    @Test
    @Transactional
    public void testNonValidToken() {
        JwtTokenUtil jwt = new JwtTokenUtil(tokenRevocationRepository);

        // Changing the token
        String token = jwt.createJWT(user.getUserId()) + "UwU";

        try {
            jwt.isExpired(token);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(BadCredentialsException.class));
        }
    }

    @Test
    @Transactional
    public void testRevokedToken() {
        String token = jwt.createJWT(user.getUserId());

        Claims claims = jwt.isExpired(token);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, claims, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);

        jwt.expire();

        try {
            jwt.isExpired(token);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(BadCredentialsException.class));
        }
    }
}