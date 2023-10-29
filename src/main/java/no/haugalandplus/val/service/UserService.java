package no.haugalandplus.val.service;

import no.haugalandplus.val.constants.UserTypeEnum;
import no.haugalandplus.val.dto.CreateUserDTO;
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

    public UserDTO insertUser(CreateUserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException(userDTO.getUsername()
                    + " already exist, please choose something else!");
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUsername(userDTO.getUsername());
        user.setUserType(UserTypeEnum.USER);
        return convertToDTO(userRepository.save(user));
    }

    public void removeUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if(userRepository.existsById(userDTO.getUserId())) {
            throw new NoSuchElementException("User with ID: "
                    + userDTO.getUserId() + " is not found!");
        }
        User updatedUser = convertToUser(userDTO);
        User existingUser = userRepository.findById(userDTO.getUserId())
                .orElseThrow(NoSuchElementException::new);

        updatedUser.setPassword(existingUser.getPassword());

        updatedUser = userRepository.save(updatedUser);

        return convertToDTO(updatedUser);
    }

    public UserDTO getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
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
