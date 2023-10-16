package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ChoiceId;

    @ManyToOne
    @JoinColumn(name = "choice_poll_id")
    private Poll poll;

    private String name;
    private String description;
}
