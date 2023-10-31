package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.constants.PollStatusEnum;

import java.util.List;

@Getter
@Setter
public class PollDTO {
    private long pollId;
    //private long userId;
    private String name;
    private String description;
    private boolean anon;
    private PollStatusEnum status;
    private List<ChoiceDTO> choices;
}
