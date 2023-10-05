package no.haugalandplus.val.dto;

import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.entities.Poll;

@Getter
@Setter
public class PollChoiceDTO {

    private Long pollChoiceId;
    private long pollId;
    private String name;
    private String description;
}
