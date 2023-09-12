package no.haugalandplus.val;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Config config;

    private long choice0Count;

    private long choice1Count;

    private String title;

    private String description;
}
