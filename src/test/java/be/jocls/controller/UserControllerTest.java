package be.jocls.controller;


import be.jocls.application.service.UserService;
import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import be.jocls.application.service.UserRegistrationService;
import be.jocls.infrastructure.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser
    void registerUser() throws Exception {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .userRole(UserRole.STUDENT)
                .build();
        User savedUser = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .userRole(UserRole.STUDENT)
                .build();

        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.username").value(savedUser.getUsername()));
    }

    @Test
    @WithMockUser
    void getUserByUsername_userExists() throws Exception {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .userRole(UserRole.STUDENT)
                .build();
        Mockito.when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser
    void getUserByUsername_userDoesNotExist() throws Exception {
        Mockito.when(userService.getUserByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/unknown"))
                .andExpect(status().isNotFound());
    }
}
