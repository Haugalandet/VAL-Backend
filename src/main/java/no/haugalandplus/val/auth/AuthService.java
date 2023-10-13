package no.haugalandplus.val.auth;

import no.haugalandplus.val.dto.LoginDTO;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.service.Utils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends Utils {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwrTokenUtil;


    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwrTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwrTokenUtil = jwrTokenUtil;
    }

    public String createToken(LoginDTO user) {
        // Create an Authentication object with the provided credentials.
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        );

        // Attempt to authenticate the user.
        Authentication authResult = authenticationManager.authenticate(authentication);

        // Creates a token that the user can use
        return jwrTokenUtil.createJWT(((User) authResult.getPrincipal()).getUserId());
    }

    public String createToken() {
        return jwrTokenUtil.createJWT(getCurrentUser().getUserId());
    }
}
