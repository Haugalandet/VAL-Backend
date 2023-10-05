package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PollResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pollResultId;

    @ManyToOne
    private PollInst pollInst;

    @ManyToOne
    private Choice choice;

    private long totalCount;
}
