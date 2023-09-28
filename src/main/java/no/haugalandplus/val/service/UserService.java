package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.UserDTO;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User insertUser(User user) {
        User existing = userRepository.getUserByUsername(user.getUsername());
        if(existing != null) {
            throw new RuntimeException("fuck deg, du suger!!!!");
        }
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convert).toList();
    }

    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setUserId(user.getUserId());
        return userDTO;
    }

}
