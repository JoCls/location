package be.jocls.infrastructure.controller;

import be.jocls.application.dto.ItemAvailabilityRequestDTO;
import be.jocls.application.dto.ItemAvailabilityResponseDTO;
import be.jocls.domain.model.Item;
import be.jocls.application.service.ItemService;
import be.jocls.domain.model.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;


    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        return ResponseEntity.ok(itemService.createItem(item));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ItemAvailabilityResponseDTO>> getAvailableItems(
            @RequestParam("startTime") LocalDateTime startTime,
            @RequestParam("endTime") LocalDateTime endTime,
            @RequestParam("type") String type) {

        ItemAvailabilityRequestDTO itemAvailabilityRequest = new ItemAvailabilityRequestDTO(startTime, endTime, ItemType.valueOf(type.toUpperCase()));

        // Fetch available items
        List<Item> availableItems = itemService.getAvailableItems(itemAvailabilityRequest);

        // Map items to response DTOs
        List<ItemAvailabilityResponseDTO> responseDTOs = availableItems.stream()
                .map(ItemAvailabilityResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.findAll();
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable Long id, @RequestBody Item updatedItem) {
        try {
            itemService.updateItem(id, updatedItem);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItemById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
