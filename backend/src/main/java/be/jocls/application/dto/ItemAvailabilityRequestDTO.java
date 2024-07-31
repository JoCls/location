package be.jocls.application.dto;

import be.jocls.domain.model.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAvailabilityRequestDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ItemType itemType;
}
