package no.haugalandplus.val;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE)
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
