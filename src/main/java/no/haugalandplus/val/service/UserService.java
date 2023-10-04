package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.UserDTO;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private UserRepository userRepository;

    private long id = 0L;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User insertUser(User user) {
        User existing = userRepository.getUserByUsername(user.getUsername());
        if(existing != null) {
            throw new NoSuchElementException(user.getUsername()
                    + " already exist, please choose something else!");
        }
        return userRepository.save(user);
    }


    //ER IKKE FERDIG MED DENNE METODEN BTW
    public UserDTO removeUser(UserDTO user) {
        long userId = user.getUserId();
        Boolean existing = userRepository.existsById(userId);
        if(!existing) {
            throw new NoSuchElementException("User with ID: "
                    + userId + " is not found!");
        }
        userRepository.deleteById(userId);
        return user;
    }

    public UserDTO getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
        return convert(user);
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
