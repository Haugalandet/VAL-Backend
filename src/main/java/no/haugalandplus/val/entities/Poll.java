package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pollId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Config config;

    private long choice0Count = 0;

    private long choice1Count = 0;

    private String title;

    private String description;

    @OneToMany(mappedBy = "poll")
    private Set<Vote> votes = new HashSet<>();

    public Poll(User user, Config config, String title, String description) {
        this.user = user;
        this.config = config;
        this.title = title;
        this.description = description;
    }

    public Poll() {}
}
