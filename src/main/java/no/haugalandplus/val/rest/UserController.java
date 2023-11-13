    package no.haugalandplus.val.rest;

    import no.haugalandplus.val.dto.CreateUserDTO;
    import no.haugalandplus.val.dto.UserDTO;
    import no.haugalandplus.val.entities.User;
    import no.haugalandplus.val.repository.UserRepository;
    import no.haugalandplus.val.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.ArrayList;
    import java.util.List;

    @RestController
    @RequestMapping("/users")
    public class UserController {

        private final UserService userservice;

        public UserController(UserService userservice) {
            this.userservice = userservice;
        }

        @PostMapping
        public UserDTO createUser(@RequestBody CreateUserDTO user) {
            return userservice.insertUser(user);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("@authService.isLoggedInUser(#id)")
        public void deleteUser(@PathVariable Long id) {
            userservice.removeUser(id);
        }

        @PutMapping("/{id}")
        @PreAuthorize("@authService.isLoggedInUser(#id)")
        public UserDTO updateUser(@RequestBody UserDTO user) {
            return userservice.updateUser(user);
        }

        @GetMapping("/{id}")
        @PreAuthorize("@authService.isLoggedInUser(#id)")
        public UserDTO getUser(@PathVariable Long id) {
            return userservice.getUser(id);
        }
    }
