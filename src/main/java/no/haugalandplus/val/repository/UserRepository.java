package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);
}
