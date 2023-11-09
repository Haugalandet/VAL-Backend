package no.haugalandplus.val.repository;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByUser(User user);

    boolean existsByPollIdAndUser(Long pollId, User user);

    //boolean existsByPollIdAndStatusAndAndIotListContains(Long pollId, User iot, PollStatusEnum status);
}
