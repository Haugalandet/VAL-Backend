package no.haugalandplus.val.service;

import no.haugalandplus.val.entities.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtils {

    protected User getCurrentUserSafe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        if (auth.getPrincipal() instanceof User user) {
            return user;
        }
        return null;
    }

    protected User getCurrentUser() {
        User user = getCurrentUserSafe();
        if (user == null) {
            throw new AccessDeniedException("Not logged in");
        }
        return user;
    }
}
