package be.jocls.application.dto;

import be.jocls.domain.model.Item;
import be.jocls.domain.model.ItemType;
import be.jocls.domain.model.ReservationStatus;
import be.jocls.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private User user;
    private Item item;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus reservationStatus;
}
