package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.UserDTO;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.apache.el.parser.BooleanNode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            throw new RuntimeException(user.getUsername()
                    + " already exist, please choose something else!");
        }
        return userRepository.save(user);
    }

    public void removeUser(UserDTO user) {
        long userId = user.getUserId();
        Boolean existing = userRepository.existsById(userId);
        if(!existing) {
            throw new NoSuchElementException("User with ID: "
                    + userId + " is not found!");
        }
        userRepository.deleteById(userId);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        Boolean existing = userRepository.existsById(userDTO.getUserId());
        if(!existing) {
            throw new NoSuchElementException("User with ID: "
                    + userDTO.getUserId() + " is not found!");
        }
        User updatedUser = convertToUser(userDTO);
        User existingUser = userRepository.findById(userDTO.getUserId()).get();

        existingUser.setPassword(existingUser.getPassword());
        existingUser.setUsername(updatedUser.getUsername());
        return convertToDTO(existingUser);
    }

    public UserDTO getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
        return convertToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO).toList();
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setUserId(user.getUserId());
        return userDTO;
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(user.getUsername());
        user.setUserId(userDTO.getUserId());
        return user;
    }

}
