package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.constants.PollStatusEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(indexes = @Index(columnList = "roomCode"))
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pollId;

    private String title;
    private String description;
    private boolean needLogin;

    private Date startTime;
    private Date endTime;

    private String roomCode;

    private PollStatusEnum status;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "choice_poll_id")
    private List<Choice> choices;

    @OneToMany
    private List<User> iotList = new ArrayList<>();
}
