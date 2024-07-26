package be.jocls.controller;

import be.jocls.application.dto.LoginRequestDTO;
import be.jocls.application.service.UserService;
import be.jocls.infrastructure.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    /*
    @Test
    public void testLogin_Success() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        when(userService.authenticate("testuser", "password")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Login successful"));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");
        when(userService.authenticate("testuser", "password")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("Invalid username or password"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testSessionManagement() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/some-protected-endpoint"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
     */
}