package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.UserDTO;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO insertUser(User user) {
        User existing = userRepository.getUserByUsername(user.getUsername());
        if(existing != null) {
            throw new RuntimeException(user.getUsername()
                    + " already exist, please choose something else!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return convertToDTO(userRepository.save(user));
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

        updatedUser.setPassword(existingUser.getPassword());

        updatedUser = userRepository.save(updatedUser);

        return convertToDTO(updatedUser);
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
