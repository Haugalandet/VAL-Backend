package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Boolean existsByVoterAndPoll(User voter, Poll poll);

    @Query("select coalesce(sum(v.voteCount), 0) from Vote v where v.choice.ChoiceId = :choiceId and v.poll.pollId = :pollId")
    Long sumOfVotesByChoiceIdAndPollInstId(
            @Param("choiceId") Long choiceId, @Param("pollId") Long pollId);
}
