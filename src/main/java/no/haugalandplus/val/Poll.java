package no.haugalandplus.val;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class Poll {
    @Id
    private long pollId;

    @OneToOne
    private User user;

    @OneToOne
    private Config config;

    private long choice0Count;

    private long choice1Count;

    private String title;

    private String description;
}
