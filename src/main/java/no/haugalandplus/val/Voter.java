package no.haugalandplus.val;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
public class Voter {
    @Id
    private long voterId;

    @ManyToOne
    private Poll poll;

    @OneToMany
    private Set<User> user;
}
