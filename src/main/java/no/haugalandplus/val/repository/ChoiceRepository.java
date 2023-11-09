package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {}
