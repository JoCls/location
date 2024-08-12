package be.jocls.infrastructure.controller;

import be.jocls.application.dto.CreateReservationRequestDTO;
import be.jocls.application.dto.ReservationDTO;
import be.jocls.application.dto.ReservationStatusRequestDTO;
import be.jocls.domain.model.Item;
import be.jocls.domain.model.Reservation;
import be.jocls.application.service.ReservationService;
import be.jocls.domain.model.ReservationStatus;
import be.jocls.domain.model.User;
import be.jocls.domain.repository.ItemRepository;
import be.jocls.domain.repository.UserRepository;
import be.jocls.infrastructure.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody CreateReservationRequestDTO request) {

        // Get the current user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new IllegalArgumentException("Item not found"));


        // Check for conflicts
        boolean hasConflict = reservationService.hasConflict(
                request.getStartTime(),
                request.getEndTime(),
                request.getItemId()
        );

        if (hasConflict) {
            return ResponseEntity.badRequest().body(null);
        }

        Reservation reservation = Reservation.builder()
                .item(item)
                .user(user)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .reservationStatus(ReservationStatus.APPROVED)
                .build();


        Reservation createdReservation = reservationService.createReservation(reservation);
        ReservationDTO responseDTO = reservationMapper.toDto(createdReservation);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/user")
    public ResponseEntity<List<ReservationDTO>> getUserReservations() {
        // Get the current user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("username when listing reservation: " + username);

        List<Reservation> reservations = reservationService.getReservationsByUsername(username);

        // Convert the reservations to DTOs
        List<ReservationDTO> reservationDTOs = reservations.stream()
                .map(reservationMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(reservationDTOs, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservation -> {
                    ReservationDTO responseDTO = reservationMapper.toDto(reservation);
                    return ResponseEntity.ok(responseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Reservation> updateReservationStatus(@PathVariable Long id, @RequestBody ReservationStatusRequestDTO request) {
        try {
            reservationService.updateReservationStatus(id, request.getReservationStatus());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
