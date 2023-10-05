package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.entities.PollResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollResultRepository extends JpaRepository<PollResult, Long> {
    List<PollResult> findPollResultsByPollInst(PollInst pollInst);
}
