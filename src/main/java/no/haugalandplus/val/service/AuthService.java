package no.haugalandplus.val.service;

import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.PollRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends ServiceUtils {

    private final PollRepository pollRepository;

    public AuthService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Boolean isLoggedInUser(Long id) {
        User user = getCurrentUserSafe();
        if (user == null) {
            return false;
        }
        return getCurrentUser().getUserId() == id;
    }

    public Boolean isLoggedIn() {
        return getCurrentUserSafe() != null;
    }

    public Boolean ownsPoll(Long id) {
        return isLoggedIn() && pollRepository.existsByPollIdAndUser(id, getCurrentUser());
    }
}
