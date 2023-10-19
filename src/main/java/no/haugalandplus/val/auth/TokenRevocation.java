package no.haugalandplus.val.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class TokenRevocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenRevocation;

    private String jwtId;

    private Date expirationTime;

    public TokenRevocation(String jwtId, Date expirationTime) {
        this.jwtId = jwtId;
        this.expirationTime = expirationTime;
    }


    public TokenRevocation() {}
}
