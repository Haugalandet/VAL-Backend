package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.PollInst;
import no.haugalandplus.val.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByChoiceAndPollInst(Choice choice, PollInst pollInst);

}
