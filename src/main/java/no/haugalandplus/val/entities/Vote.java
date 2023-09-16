package no.haugalandplus.val.entities;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteId;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Poll poll;

    @ManyToOne
    private User voter;

    private Integer voteCount;

    private Boolean choice;

    public Vote(Poll poll, User voter, Boolean choice) {
        this.poll = poll;
        this.voter = voter;
        this.choice = choice;
        this.voteCount = 1;
    }

    public Vote() {}

}
