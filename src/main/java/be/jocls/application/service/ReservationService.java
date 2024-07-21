package be.jocls.application.service;

import be.jocls.domain.model.*;
import be.jocls.domain.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> findConflictingReservations(Item item, LocalDateTime startTime, LocalDateTime endTime) {
        return reservationRepository.findByItemAndEndTimeAfterAndStartTimeBefore(item, endTime, startTime);
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }


    public Reservation createReservation(Reservation reservation) {
        User user = reservation.getUser();
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Check role-specific logic for reservation types
        if (user.getUserRole() == UserRole.STUDENT && reservation.getItem().getItemType() != ItemType.EQUIPMENT) {
            throw new RuntimeException("Students can only reserve equipment");
        }

        if (user.getUserRole() == UserRole.TEACHER &&
                (reservation.getItem().getItemType() != ItemType.EQUIPMENT && reservation.getItem().getItemType() != ItemType.CLASSROOM)) {
            throw new RuntimeException("Teachers can only reserve equipment or classrooms");
        }

        // Set reservation status and save
        reservation.setReservationStatus(ReservationStatus.APPROVED);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUsername(String username) {
        return reservationRepository.findByUserUsername(username);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
