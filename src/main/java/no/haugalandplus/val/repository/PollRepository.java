package no.haugalandplus.val.repository;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findAllByUser(User user);
    boolean existsByPollIdAndUser(Long pollId, User user);
    boolean existsByPollIdAndIotListContainingAndStatus(Long pollId, User user, PollStatusEnum status);
    boolean existsByRoomCodeAndStatus(String roomCode, PollStatusEnum status);
    Poll findByRoomCode(String roomCode);

    @Transactional
    @Modifying
    @Query("delete from Poll p where p.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    void deleteAllByPollId(Long pollId);

}
