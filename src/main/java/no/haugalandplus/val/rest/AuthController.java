package no.haugalandplus.val.rest;

import no.haugalandplus.val.auth.TokenService;
import no.haugalandplus.val.auth.JwtTokenUtil;
import no.haugalandplus.val.dto.LoginDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private TokenService tokenService;
    private JwtTokenUtil jwrTokenUtil;

    public AuthController(TokenService tokenService, JwtTokenUtil jwrTokenUtil) {
        this.tokenService = tokenService;
        this.jwrTokenUtil = jwrTokenUtil;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.createToken(user));
        return ResponseEntity.ok().headers(headers).body("Authentication successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> login() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenService.createToken());

        return ResponseEntity.ok().headers(headers).body("Authentication successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> signOut() {
        jwrTokenUtil.expire();
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody String s) {
        StringBuilder sb = new StringBuilder(s);
        return ResponseEntity.ok(sb.reverse().toString());
    }
}
