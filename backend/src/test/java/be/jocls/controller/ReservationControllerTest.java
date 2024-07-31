package be.jocls.controller;

import be.jocls.application.dto.CreateReservationRequestDTO;
import be.jocls.application.dto.ReservationDTO;
import be.jocls.domain.model.*;
import be.jocls.application.service.ReservationService;
import be.jocls.domain.repository.UserRepository;
import be.jocls.infrastructure.config.JwtUtil;
import be.jocls.infrastructure.controller.ReservationController;
import be.jocls.infrastructure.mapper.ReservationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User studentUser;
    private Item item;
    private Reservation reservation;
    private CreateReservationRequestDTO reservationDTO;
    private String jwtToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock user
        studentUser = User.builder()
                .id(1L)
                .username("studentuser")
                .password("password")
                .email("student@example.com")
                .userRole(UserRole.STUDENT)
                .build();

        // Setup item
        item = Item.builder()
                .id(1L)
                .name("Laptop")
                .itemType(ItemType.EQUIPMENT)
                .description("Lenovo laptop")
                .build();

        // Setup reservation
        reservation = Reservation.builder()
                .id(1L)
                .user(studentUser)
                .item(item)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.APPROVED)
                .build();

        // Setup reservation DTO
        reservationDTO = CreateReservationRequestDTO.builder()
                .itemId(item.getId())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .build();

        // Mock JWT generation
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                studentUser.getUsername(),
                studentUser.getPassword(),
                List.of(new SimpleGrantedAuthority(studentUser.getUserRole().name()))
        );

        jwtToken = "mockedJwtToken";
        Mockito.when(jwtUtil.generateToken("studentuser", UserRole.STUDENT)).thenReturn(jwtToken);
    }

    private String getAuthHeader() {
        return "Bearer " + jwtToken;
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    public void createReservation() throws Exception {
        Mockito.when(userRepository.findByUsername("studentuser")).thenReturn(Optional.of(studentUser));
        Mockito.when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        String jsonContent = objectMapper.writeValueAsString(reservationDTO);

        mockMvc.perform(post("/api/reservations/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getAuthHeader())
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.item.name").value(reservation.getItem().getName()));
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    public void getReservation() throws Exception {
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/api/reservations/1")
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.item.name").value(reservation.getItem().getName()));
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    public void getReservation_notFound() throws Exception {
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/1")
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    public void getUserReservations() throws Exception {
        List<Reservation> reservations = Collections.singletonList(reservation);
        Mockito.when(reservationService.getReservationsByUsername(any(String.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/user")
                        .header("Authorization", getAuthHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()))
                .andExpect(jsonPath("$[0].item.name").value(reservation.getItem().getName()));
    }
}
