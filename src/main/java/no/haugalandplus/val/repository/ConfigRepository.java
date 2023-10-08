package no.haugalandplus.val.repository;

import no.haugalandplus.val.entities.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {
}
