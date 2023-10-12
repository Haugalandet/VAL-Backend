package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.LoginDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO user) {
        try {
            // Create an Authentication object with the provided credentials.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
            );

            // Attempt to authenticate the user.
            Authentication authResult = authenticationManager.authenticate(authentication);

            // Set the authenticated user's security context.
            SecurityContextHolder.getContext().setAuthentication(authResult);

            return ResponseEntity.ok("Authentication successful");
        } catch (AuthenticationException e) {
            // Handle authentication failure (e.g., invalid credentials).
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/test")
    public String test(@RequestBody String s) {
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }
}
