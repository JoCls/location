package be.jocls.controller;

import be.jocls.domain.model.*;
import be.jocls.domain.service.ReservationService;
import be.jocls.infrastructure.controller.ReservationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReservation() throws Exception {
        User user = new User(1L, "testuser", "password", "test@example.com", UserRole.STUDENT);
        Item item = new Item(1L, "Laptop", ItemType.EQUIPMENT, "Lenovo laptop");
        Reservation reservation = new Reservation(null, user, item, LocalDateTime.now(), LocalDateTime.now().plusHours(2), ReservationStatus.APPROVED);
        Reservation savedReservation = new Reservation(1L, user, item, reservation.getStartTime(), reservation.getEndTime(), ReservationStatus.APPROVED);

        Mockito.when(reservationService.createReservation(any(Reservation.class))).thenReturn(savedReservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedReservation.getId()))
                .andExpect(jsonPath("$.item.name").value(savedReservation.getItem().getName()));
    }

    @Test
    void getReservation() throws Exception {
        User user = new User(1L, "testuser", "password", "test@example.com", UserRole.STUDENT);
        Item item = new Item(1L, "Laptop", ItemType.EQUIPMENT, "Lenovo laptop");
        Reservation reservation = new Reservation(1L, user, item, LocalDateTime.now(), LocalDateTime.now().plusHours(2), ReservationStatus.APPROVED);

        Mockito.when(reservationService.getReservation(1L)).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.item.name").value(reservation.getItem().getName()));
    }

    @Test
    void getReservation_notFound() throws Exception {
        Mockito.when(reservationService.getReservation(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isNotFound());
    }
}
