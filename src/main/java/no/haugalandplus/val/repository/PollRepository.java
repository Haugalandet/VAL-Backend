package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByAnonIsFalse();

    List<Poll> findAllByUser(User user);
}
