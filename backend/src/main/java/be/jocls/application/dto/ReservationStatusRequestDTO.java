package be.jocls.application.dto;


import be.jocls.domain.model.ReservationStatus;
import be.jocls.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusRequestDTO {
    private ReservationStatus reservationStatus;
}
