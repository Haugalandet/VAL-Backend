package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.UserDTO;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import no.haugalandplus.val.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userservice;

    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userservice.insertUser(user);
    }
    @DeleteMapping("/{id}")
    public UserDTO deleteUser(@PathVariable Long id) {
        return userservice.removeUser(getUser(id));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userservice.getUser(id);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userservice.getAllUsers();
    }
}
