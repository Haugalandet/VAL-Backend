package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class StartPollDTO {
    private Date startTime;
    private Date endTime;
}
