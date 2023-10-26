package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.PollInst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollInstRepository extends JpaRepository<PollInst, Long> {

    @Query("select pi from PollInst pi where pi.poll.pollId = :pollId")
    List<PollInst> findAllByPollId(@Param("pollId") long pollId);
}
