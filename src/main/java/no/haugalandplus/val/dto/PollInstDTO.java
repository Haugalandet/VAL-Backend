package no.haugalandplus.val.dto;

import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.entities.Poll;

import java.time.LocalDateTime;

@Getter
@Setter
public class PollInstDTO {
    private long pollInstId;

    private long pollId;

    private String title;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String roomCode;
}
