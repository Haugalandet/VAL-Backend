package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Long> {
}
