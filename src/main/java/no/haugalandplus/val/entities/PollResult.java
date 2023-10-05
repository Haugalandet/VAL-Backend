package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private PollChoice pollChoice;

    private long totalCount;
}
