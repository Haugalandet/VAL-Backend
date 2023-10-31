package no.haugalandplus.val.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteId;

    @ManyToOne
    private Poll poll;

    @ManyToOne
    private User voter;

    @ManyToOne
    private Choice choice;

    private Integer voteCount;

}
