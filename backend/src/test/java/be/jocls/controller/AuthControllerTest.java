package be.jocls.controller;

import be.jocls.application.dto.LoginRequestDTO;
import be.jocls.application.dto.LoginResponseDTO;
import be.jocls.application.service.UserService;
import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import be.jocls.infrastructure.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        User mockUser = new User();
        mockUser.setEmail("testuser");
        mockUser.setPassword("password");
        mockUser.setUserRole(UserRole.STUDENT);

        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        when(userService.authenticate("testuser", "password")).thenReturn(mockUser);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof LoginResponseDTO);

        LoginResponseDTO responseBody = (LoginResponseDTO) response.getBody();
        assertEquals("STUDENT", responseBody.getUserRole());

    }

    @Test
    public void testLogin_Failure() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        when(userService.authenticate("testuser", "password")).thenReturn(null);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }
}
