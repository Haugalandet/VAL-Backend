package no.haugalandplus.val.config;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import no.haugalandplus.val.entities.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class JwtTokenUtilTest {

    @Test
    public void testValidation() {
        String testName = "Ole Jonny";
        long userId = 34;

        User user = new User();
        user.setUsername(testName);
        user.setUserId(userId);

        JwtTokenUtil jwt = new JwtTokenUtil();

        String token = jwt.createJWT(user);

        Claims claims = jwt.claimsFromJwt(token);

        assertThat(claims.getSubject(), is(userId + ""));
    }
}