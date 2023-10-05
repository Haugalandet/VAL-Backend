package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollResultDTO {
    private ChoiceDTO pollChoice;
    private long totalCount;
}
