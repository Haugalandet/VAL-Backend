package no.haugalandplus.val.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface TokenRevocationRepository extends JpaRepository<TokenRevocation, Long> {
    boolean existsByJwtId(@Param("jwtId") String jwtId);
    void deleteAllByExpirationTimeBefore(Date date);

}
