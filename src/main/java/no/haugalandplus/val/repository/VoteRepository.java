package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select (count(v) > 0) from Vote v where v.choice.poll = :poll and v.voter = :voter")
    Boolean existsByVoterAndPoll(@Param("voter") User voter, @Param("poll") Poll poll);

    @Query("select coalesce(sum(v.voteCount), 0) from Vote v where v.choice.ChoiceId = :choiceId")
    Long sumOfVotesByChoiceId(@Param("choiceId") Long choiceId);

    @Transactional
    @Modifying
    @Query("delete from Vote v where v.choice.poll = :poll")
    void deleteAllByPoll(@Param("poll") Poll poll);

    @Transactional
    @Modifying
    @Query("delete from Vote v where v.choice.poll.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("update Vote v set v.voter = null where v.voter.userId = :userId")
    void anonymizeAllVotesWithUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("delete from Vote v where v.choice.poll.pollId = :pollId")
    void deleteAllByPollId(@Param("pollId") Long pollId);
}
