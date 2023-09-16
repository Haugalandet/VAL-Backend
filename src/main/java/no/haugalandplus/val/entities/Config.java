package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Config(long configId, User owner) {
        this.configId = configId;
        this.owner = owner;
        anon = true;
    }

    public Config() {

    }
}
