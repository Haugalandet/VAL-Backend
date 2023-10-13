package no.haugalandplus.val.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filter used to add security context
 */
@Component
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private UserRepository userRepository;

    private JwtTokenUtil jwtTokenUtil;

    public JwtTokenAuthenticationFilter(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null) {
            try {

                // Adds user to security context
                Claims claims = jwtTokenUtil.isExpired(token);
                User user = userRepository.findById(Long.parseLong(claims.getSubject())).get();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, claims, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                // TODO get a proper error message
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken;
        }
        return null;
    }
}
