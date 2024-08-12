package be.jocls.controller;


import be.jocls.application.service.UserRegistrationService;
import be.jocls.application.service.UserService;
import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import be.jocls.infrastructure.config.JwtRequestFilter;
import be.jocls.infrastructure.config.JwtUtil;
import be.jocls.infrastructure.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRegistrationService userRegistrationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock user
        user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .userRole(UserRole.STUDENT)
                .build();

        // Mock JWT token
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().name()))
        );

        jwtToken = "mockedJwtToken";
        Mockito.when(jwtUtil.generateToken(user.getUsername(), user.getUserRole())).thenReturn(jwtToken);
    }

    private String getAuthHeader() {
        return "Bearer " + jwtToken;
    }
}
