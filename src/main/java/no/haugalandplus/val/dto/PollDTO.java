package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollDTO {
    private long pollId;
    private long userId;
    private long configId;
    private String name;
    private String description;
    private boolean anon;
}
