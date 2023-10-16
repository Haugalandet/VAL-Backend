package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PollDTO {
    private long pollId;
    //private long userId;
    private String name;
    private String description;
    private boolean anon;
    private List<ChoiceDTO> choices;
}
