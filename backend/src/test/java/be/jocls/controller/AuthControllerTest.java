package be.jocls.controller;

import be.jocls.application.dto.LoginRequestDTO;
import be.jocls.application.dto.LoginResponseDTO;
import be.jocls.application.service.UserService;
import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import be.jocls.infrastructure.config.JwtUtil;
import be.jocls.infrastructure.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    JwtUtil jwtUtil;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        User mockUser = new User();
        mockUser.setEmail("testuser");
        mockUser.setPassword("password");
        mockUser.setUserRole(UserRole.STUDENT);

        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                mockUser.getEmail(),
                mockUser.getPassword(),
                List.of(new SimpleGrantedAuthority(mockUser.getUserRole().name()))
        );

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        // Mock authentication
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword())))
                .thenReturn(authenticationToken);

        // Mock user service authentication
        when(userService.authenticate("testuser", "password")).thenReturn(mockUser);

        // Mock JWT token generation
        String expectedToken = "mockedJwtToken";
        when(jwtUtil.generateToken("testuser", UserRole.STUDENT)).thenReturn(expectedToken);

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof LoginResponseDTO);

        LoginResponseDTO responseBody = (LoginResponseDTO) response.getBody();

        // Assert JWT token is present in the response
        assertEquals(expectedToken, responseBody.getToken());
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
