package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PollInstDTO {
    private Long pollInstId;

    private Long pollId;

    private String name;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String roomCode;

    private List<PollResultDTO> pollResult;
}
