package no.haugalandplus.val.dto;

import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.entities.User;

@Getter
@Setter
public class ConfigDTO {
    private long configId;
    private long ownerId;
    private String configName;
    private String description;
    private boolean anon;
}
