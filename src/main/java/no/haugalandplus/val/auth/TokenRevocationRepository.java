package no.haugalandplus.val.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRevocationRepository extends JpaRepository<TokenRevocation, Long> {
    boolean existsByJwtId(String id);
    void deleteAllByExpirationTimeBefore(Date date);

}
