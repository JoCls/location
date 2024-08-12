package be.jocls.application.dto;

import be.jocls.domain.model.Item;
import be.jocls.domain.model.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAvailabilityResponseDTO {
    private Long id;
    private String name;
    private ItemType itemType;
    private String description;

    // Convert from Item entity to DTO
    public static ItemAvailabilityResponseDTO fromEntity(Item item) {
        ItemAvailabilityResponseDTO dto = new ItemAvailabilityResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setItemType(item.getItemType());
        dto.setDescription(item.getDescription());
        return dto;
    }
}
