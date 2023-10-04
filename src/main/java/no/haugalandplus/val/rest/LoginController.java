package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.LoginDTO;
import no.haugalandplus.val.entities.User;
import org.springframework.security.core.Authentication;
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
    public boolean login(@RequestBody LoginDTO user) {
        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword());

        System.out.println("\nAuthentication Token Before Authentication: " + token);

        Authentication authResult = authenticationManager.authenticate(token);

        System.out.println();
        System.out.println("Authentication Token After Authentication: " + authResult);
        System.out.println();

        System.out.println("Authentication Token in Security Context: " + SecurityContextHolder.getContext().getAuthentication());

        System.out.println();
        if(authResult.isAuthenticated())
            System.out.println("User is Authenticated");

        return true;
    }

    @PostMapping("/test")
    public String test() {
        return "hello %s".formatted(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}
