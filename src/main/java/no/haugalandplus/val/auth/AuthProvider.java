package no.haugalandplus.val.auth;

import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Class used to check if the username and passwords are correct
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String providedUsername = authentication.getPrincipal().toString();
        User user = userRepository.findByUsername(providedUsername);

        String providedPassword = authentication.getCredentials().toString();
        String encodedPassword = user.getPassword();

        if (!passwordEncoder.matches(providedPassword, encodedPassword))
            throw new RuntimeException("Incorrect Credentials");

        // return Authentication Object
        Authentication authenticationResult =
                new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), new ArrayList<>());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}