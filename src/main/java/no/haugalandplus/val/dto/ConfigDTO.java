package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigDTO {
    private long configId;
    private long ownerId;
    private String name;
    private String description;
    private boolean anon;
}
