package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    @Transactional
    @Modifying
    @Query("delete from Choice c where c.poll.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
