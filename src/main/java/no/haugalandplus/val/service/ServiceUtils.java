package no.haugalandplus.val.service;

import no.haugalandplus.val.entities.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtils {

    protected User getCurrentUserSafe() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new AccessDeniedException("Not logged in");
        }
        return user;
    }
}
