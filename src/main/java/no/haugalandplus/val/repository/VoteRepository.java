package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
