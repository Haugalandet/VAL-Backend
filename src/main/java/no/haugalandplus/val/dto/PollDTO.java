package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.constants.PollStatusEnum;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PollDTO {
    private long pollId;
    private String title;
    private String description;
    private boolean anon;
    private Date startTime;
    private Date endTime;
    private String roomCode;
    private PollStatusEnum status;
    private List<ChoiceDTO> choices;
    private Boolean hasUserVoted;
}
