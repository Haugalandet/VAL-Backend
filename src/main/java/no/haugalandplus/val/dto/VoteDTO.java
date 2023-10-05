package no.haugalandplus.val.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.PollChoice;
import no.haugalandplus.val.entities.User;

@Getter
@Setter
public class VoteDTO {
    private Long pollChoiceId;
    private Long voteCount;
}
