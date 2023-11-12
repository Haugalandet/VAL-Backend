package no.haugalandplus.val.auth;

import no.haugalandplus.val.TestUtils;
import no.haugalandplus.val.dto.LoginDTO;
import no.haugalandplus.val.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class TokenServiceTest extends TestUtils {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwrTokenUtil;

    private String username;
    private String password;
    private User user;

    @BeforeEach
    public void init() {
        username = "Stig Henry";
        password = "ooO oOO ooO";

        user = new User();
        user.setPassword(password);
        user.setUsername(username);

        user = saveNewUser(user);
    }

    @Test
    @Transactional
    public void testCreateToken() {
        TokenService tokenService = new TokenService(authenticationManager, jwrTokenUtil);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPassword(password);
        loginDTO.setUsername(username);

        // Checks that you get a token
        tokenService.createToken(loginDTO);

        loginDTO.setPassword(password + "lalalala");
        try {
            tokenService.createToken(loginDTO);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(BadCredentialsException.class));
        }

        setSecurityContextUser(user);

        tokenService.createToken();

        setSecurityContextUser(null);

        try {
            tokenService.createToken();
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }
}