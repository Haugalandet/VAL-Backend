package no.haugalandplus.val.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRevocationRepository extends JpaRepository<TokenRevocation, Long> {
    boolean existsByJwtId(String id);

}
