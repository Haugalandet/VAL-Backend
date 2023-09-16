package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pollId;

    @OneToOne
    private User user;

    @OneToOne
    private Config config;

    private long choice0Count = 0;

    private long choice1Count = 0;

    private String title;

    private String description;

    public Poll(long pollId, User user, Config config, String title, String description) {
        this.pollId = pollId;
        this.user = user;
        this.config = config;
        this.title = title;
        this.description = description;
    }

    public Poll() {

    }

    public void incrementChoice0(long count) {
        choice0Count += count;
    }

    public void incrementChoice1(long count) {
        choice1Count += count;
    }
}
