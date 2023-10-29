package no.haugalandplus.val.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import no.haugalandplus.val.constants.UserTypeEnum;

@Getter
@Entity
@Table(name = "tbl_user")
public class User {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    private UserTypeEnum userType;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
