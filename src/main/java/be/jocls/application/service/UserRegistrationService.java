package be.jocls.application.service;

import be.jocls.application.dto.UserRegistrationDTO;
import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final String TEACHER_CERTIFICATION_REQUIRED_VALUE = "EDU_T_42";
    private static final String TEACHER_EMAIL_DOMAIN = "@edu.be";

    public void registerUser(UserRegistrationDTO userRegistrationDTO) {

        // Check if user already exists
        if (userService.getUserByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        // Validate TEACHER role specifics
        if (userRegistrationDTO.getUserRole() == UserRole.TEACHER) {
            validateTeacherRegistration(userRegistrationDTO);
        }

        // Build and save user
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

    private void validateTeacherRegistration(UserRegistrationDTO userRegistrationDTO) {
        // Validate email domain
        if (!userRegistrationDTO.getEmail().endsWith(TEACHER_EMAIL_DOMAIN)) {
            throw new RuntimeException("Incorrect Teacher email address");
        }

        // Validate teacher certification
        if (!TEACHER_CERTIFICATION_REQUIRED_VALUE.equals(userRegistrationDTO.getTeacherCertification())) {
            throw new RuntimeException("Invalid Teacher certification");
        }
    }
}
