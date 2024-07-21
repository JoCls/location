package be.jocls.infrastructure.controller;

import be.jocls.application.dto.ReservationDTO;
import be.jocls.domain.model.Reservation;
import be.jocls.application.service.ReservationService;
import be.jocls.infrastructure.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper;

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        Reservation createdReservation = reservationService.createReservation(reservation);
        ReservationDTO responseDTO = reservationMapper.toDto(createdReservation);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<List<ReservationDTO>> getUserReservations(Authentication authentication) {
        String username = authentication.getName();
        List<Reservation> reservations = reservationService.getReservationsByUsername(username);
        List<ReservationDTO> responseDTOs = reservations.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservation -> {
                    ReservationDTO responseDTO = reservationMapper.toDto(reservation);
                    return ResponseEntity.ok(responseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
