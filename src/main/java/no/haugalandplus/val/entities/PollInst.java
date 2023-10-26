package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
public class PollInst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pollInstId;

    @ManyToOne
    private Poll poll;

    private String name;
    private String description;

    private Date startTime;
    private Date endTime;

    private String roomCode;

    public PollInst() {}
}
