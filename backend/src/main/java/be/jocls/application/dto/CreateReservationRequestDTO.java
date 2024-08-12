package be.jocls.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequestDTO {
    private Long itemId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
