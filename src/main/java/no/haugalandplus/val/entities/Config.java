package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long configId;

    @ManyToOne
    private User owner;

    private String name;

    private String description;

    private boolean anon;

    public Config(User owner) {
        this.owner = owner;
        anon = true;
    }

    public Config() {}
}
