package no.haugalandplus.val;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Config {
    @Id
    private long configId;

    @ManyToOne
    private User owner;

    @Setter
    private String configName;

    @Setter
    private String description;

    @Setter
    private String choice0Name;

    @Setter
    private String choice1Name;

    @Setter
    private boolean anon;
}
