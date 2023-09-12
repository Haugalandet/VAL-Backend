package no.haugalandplus.val;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "tbl_user")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String username;

    @Setter
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Config> configs;


    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
