package no.haugalandplus.val.dto;

import jakarta.persistence.ManyToOne;
import no.haugalandplus.val.entities.Poll;

public class PollChoiceDTO {

    private Long pollChoiceId;
    private long pollId;
    private String name;
    private String description;
}
