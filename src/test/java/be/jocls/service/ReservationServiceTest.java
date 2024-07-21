package be.jocls.service;

import be.jocls.application.service.ReservationService;
import be.jocls.domain.model.Reservation;
import be.jocls.domain.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    public ReservationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHasConflict() {
        LocalDateTime startTime = LocalDateTime.of(2024, 7, 21, 13, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 7, 21, 15, 0);
        Long itemId = 1L;

        when(reservationRepository.findConflictingReservations(startTime, endTime, itemId))
                .thenReturn(List.of(new Reservation()));

        boolean conflict = reservationService.hasConflict(startTime, endTime, itemId);
        assertTrue(conflict);
    }
}