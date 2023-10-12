package no.haugalandplus.val.rest;

import no.haugalandplus.val.config.JwtTokenAuthenticationFilter;
import no.haugalandplus.val.config.JwtTokenUtil;
import no.haugalandplus.val.dto.LoginDTO;

import no.haugalandplus.val.entities.User;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

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
    private JwtTokenUtil JwtTokenUtil;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        JwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Returns with an authentication token in the header.
     */
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

            // Creates a token that the user can use
            String token = JwtTokenUtil.createJWT((User) authResult.getPrincipal());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            return ResponseEntity.ok().headers(headers).body("Authentication successful");
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
