package be.jocls.controller;

import be.jocls.application.dto.ReservationDTO;
import be.jocls.domain.model.*;
import be.jocls.application.service.ReservationService;
import be.jocls.infrastructure.controller.ReservationController;
import be.jocls.infrastructure.mapper.ReservationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

    @Autowired
    private ReservationMapper reservationMapper;


    @Autowired
    private ObjectMapper objectMapper;

    private User studentUser;
    private Item item;
    private Reservation reservation;
    private ReservationDTO reservationDTO;

    @BeforeEach
    public void setUp() {
        studentUser = User.builder()
                .username("studentuser")
                .password("password")
                .email("student@example.com")
                .userRole(UserRole.STUDENT)
                .build();

        item = Item.builder()
                .name("Laptop")
                .itemType(ItemType.EQUIPMENT)
                .description("Lenovo laptop")
                .build();

        reservation = Reservation.builder()
                .id(1L)
                .user(studentUser)
                .item(item)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.APPROVED)
                .build();

        reservationDTO = ReservationDTO.builder()
                .user(studentUser)
                .item(item)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.APPROVED)
                .build();
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    public void createReservation() throws Exception {
        Mockito.when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);

        String jsonContent = objectMapper.writeValueAsString(reservation);
        System.out.println("Request JSON: " + jsonContent);

        mockMvc.perform(post("/api/reservations/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.item.name").value(reservation.getItem().getName()));
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    void getReservation() throws Exception {
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.item.name").value(reservation.getItem().getName()));
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    void getReservation_notFound() throws Exception {
        Mockito.when(reservationService.getReservationById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "studentuser", roles = {"STUDENT"})
    public void getUserReservations() throws Exception {
        List<Reservation> reservations = Collections.singletonList(reservation);
        Mockito.when(reservationService.getReservationsByUsername(any(String.class))).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()))
                .andExpect(jsonPath("$[0].item.name").value(reservation.getItem().getName()));
    }


}
