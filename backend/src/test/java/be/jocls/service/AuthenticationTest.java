package be.jocls.service;

import be.jocls.domain.model.User;
import be.jocls.domain.model.UserRole;
import be.jocls.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Create a test user
        User user = User.builder()
                .username("teacheruser")
                .password(passwordEncoder.encode("password"))
                .email("teacher@edu.be")
                .firstName("John")
                .lastName("Doe")
                .userRole(UserRole.TEACHER)
                .build();
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the test user
        userRepository.deleteAll();
    }

    /*
    @Test
    public void testLogin() throws Exception {

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "teacheruser")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection()) // Expect a redirect to the default success URL
                .andExpect(redirectedUrl("/")); // Default success URL
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "teacheruser")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection()) // Expect a redirect to the login page with error
                .andExpect(redirectedUrl("/login?error")); // Default failure URL
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?logout"));
    }
     */

    /*
    @Test
    public void testAccessProtectedResourceWithAuthentication() throws Exception {
        mockMvc.perform(get("/teacher/dashboard")
                        .with(httpBasic("teacheruser", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessProtectedResourceWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/teacher/dashboard"))
                .andExpect(status().is3xxRedirection()) // Expect a redirect to the login page
                .andExpect(redirectedUrlPattern("**\/login"));
    }
    */

}