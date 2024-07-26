package be.jocls.controller;

import be.jocls.application.dto.LoginRequestDTO;
import be.jocls.application.service.UserService;
import be.jocls.infrastructure.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        when(userService.authenticate("testuser", "password")).thenReturn(true);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    public void testLogin_Failure() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        when(userService.authenticate("testuser", "password")).thenReturn(false);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }
}
