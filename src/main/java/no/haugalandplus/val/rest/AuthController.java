package no.haugalandplus.val.rest;

import no.haugalandplus.val.auth.TokenService;
import no.haugalandplus.val.auth.JwtTokenUtil;
import no.haugalandplus.val.dto.LoginDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TokenService tokenService;
    private final JwtTokenUtil jwrTokenUtil;

    public AuthController(TokenService tokenService, JwtTokenUtil jwrTokenUtil) {
        this.tokenService = tokenService;
        this.jwrTokenUtil = jwrTokenUtil;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO user) {
        HttpHeaders headers = new HttpHeaders();
        String token = tokenService.createToken(user);
        headers.add("Authorization", token);
        return ResponseEntity.ok().headers(headers).body(token);
    }

    @PostMapping("/refresh")
    @PreAuthorize("@authService.isLoggedIn()")
    public ResponseEntity<String> login() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.createToken());

        return ResponseEntity.ok().headers(headers).body("Authentication successful");
    }

    @PostMapping("/logout")
    @PreAuthorize("@authService.isLoggedIn()")
    public ResponseEntity<String> signOut() {
        jwrTokenUtil.expire();
        return ResponseEntity.ok("Logout successful");
    }
}
