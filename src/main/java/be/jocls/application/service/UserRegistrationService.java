package be.jocls.application.service;

import be.jocls.application.dto.UserRegistrationDTO;
import be.jocls.domain.model.User;
import be.jocls.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void registerUser(UserRegistrationDTO userRegistrationDTO)
    {
        if (userService.getUserByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        User user = User.builder()
                .username(userRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .email(userRegistrationDTO.getEmail())
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .userRole(userRegistrationDTO.getUserRole())
                .build();

        userService.createUser(user);
    }
}
