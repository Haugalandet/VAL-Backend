package no.haugalandplus.val.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserDTO {
    private String username;
    private String password;
}
