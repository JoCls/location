package be.jocls.domain.repository;

import be.jocls.domain.model.Item;
import be.jocls.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByItemAndEndTimeAfterAndStartTimeBefore(Item item, LocalDateTime endTime, LocalDateTime startTime);

    List<Reservation> findByUserUsername(String username);

    @Query("SELECT r FROM Reservation r WHERE r.item.id = :itemId " +
            "AND (r.startTime <= :endTime AND r.endTime >= :startTime)")
    List<Reservation> findConflictingReservations(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("itemId") Long itemId);
}
