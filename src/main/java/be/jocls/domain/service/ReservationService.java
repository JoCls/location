package be.jocls.domain.service;

import be.jocls.domain.model.Item;
import be.jocls.domain.model.Reservation;
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

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservation(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
