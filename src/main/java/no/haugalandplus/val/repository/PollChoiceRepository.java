package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.PollChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollChoiceRepository extends JpaRepository<PollChoice, Long> {
}
