package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pollId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Config config;


    @OneToMany(mappedBy = "poll")
    private Set<Vote> votes = new HashSet<>();

    public Poll(User user, Config config) {
        this.user = user;
        this.config = config;
    }

    public Poll() {}
}
