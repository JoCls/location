package be.jocls.domain.repository;

import be.jocls.domain.model.Item;
import be.jocls.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByItemAndEndTimeAfterAndStartTimeBefore(Item item, LocalDateTime endTime, LocalDateTime startTime);
    List<Reservation> findByUserUsername(String username);
}
