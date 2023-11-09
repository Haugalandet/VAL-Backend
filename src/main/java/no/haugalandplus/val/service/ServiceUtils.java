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
        return (User) auth.getPrincipal();
    }

    protected User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new AccessDeniedException("Not logged in");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new AccessDeniedException("Not logged in");
        }
        return user;
    }
}
