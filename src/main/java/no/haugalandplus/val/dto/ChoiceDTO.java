package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChoiceDTO {

    private Long ChoiceId;
    private String title;
    private String description;
    private Long voteCount;
}
