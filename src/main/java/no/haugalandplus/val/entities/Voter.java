package no.haugalandplus.val.entities;


import jakarta.persistence.*;
import lombok.Getter;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Voter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voterId;

    @ManyToOne
    private Poll poll;

    @OneToMany
    private Set<User> voters = new HashSet<>();


    public Voter(Poll poll) {
        this.poll = poll;
    }

    public Voter() {

    }

    public void addUser(User user) {
        voters.add(user);
    }
}
