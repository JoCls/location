package be.jocls.controller;


import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import be.jocls.domain.service.UserService;
import be.jocls.infrastructure.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser() throws Exception {
        User user = new User(null, "testuser", "password", "test@example.com", UserRole.STUDENT);
        User savedUser = new User(1L, "testuser", "password", "test@example.com", UserRole.STUDENT);

        Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.username").value(savedUser.getUsername()));
    }

    @Test
    void getUserByUsername_userExists() throws Exception {
        User user = new User(1L, "testuser", "password", "test@example.com", UserRole.STUDENT);
        Mockito.when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void getUserByUsername_userDoesNotExist() throws Exception {
        Mockito.when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/unknown"))
                .andExpect(status().isNotFound());
    }
}
