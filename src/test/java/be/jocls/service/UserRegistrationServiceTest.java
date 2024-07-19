package be.jocls.service;

import be.jocls.application.dto.UserRegistrationDTO;
import be.jocls.application.service.UserRegistrationService;
import be.jocls.application.service.UserService;
import be.jocls.domain.model.UserRole;
import be.jocls.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Test
    void registerTeacherWithValidEmailAndCertification() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("teacheruser")
                .password("password")
                .email("teacher@edu.be")
                .firstName("John")
                .lastName("Doe")
                .userRole(UserRole.TEACHER)
                .teacherCertification("EDU_T_42")
                .build();

        when(userService.getUserByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");


        userRegistrationService.registerUser(dto);
    }

    @Test
    void registerTeacherWithInvalidEmail() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("teacheruser")
                .password("password")
                .email("teacher@wrongdomain.com")
                .firstName("John")
                .lastName("Doe")
                .userRole(UserRole.TEACHER)
                .teacherCertification("EDU_T_42")
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRegistrationService.registerUser(dto);
        });
        assertEquals("Incorrect Teacher email address", exception.getMessage());
    }

    @Test
    void registerTeacherWithInvalidCertification() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .username("teacheruser")
                .password("password")
                .email("teacher@edu.be")
                .firstName("John")
                .lastName("Doe")
                .userRole(UserRole.TEACHER)
                .teacherCertification("INVALID_CERT")
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRegistrationService.registerUser(dto);
        });
        assertEquals("Invalid Teacher certification", exception.getMessage());
    }
}
