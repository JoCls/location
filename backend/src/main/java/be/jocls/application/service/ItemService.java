package be.jocls.application.service;

import be.jocls.application.dto.ItemAvailabilityRequestDTO;
import be.jocls.domain.model.Item;
import be.jocls.domain.model.ItemType;
import be.jocls.domain.model.Reservation;
import be.jocls.domain.repository.ItemRepository;
import be.jocls.domain.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getAvailableItems(ItemAvailabilityRequestDTO request) {
        LocalDateTime startDateTime = request.getStartTime();
        LocalDateTime endDateTime = request.getEndTime();
        ItemType itemType = request.getItemType();

        List<Item> allItems = itemRepository.findByItemType(itemType);

        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(
                startDateTime, endDateTime);

        List<Long> reservedItemIds = conflictingReservations.stream()
                .map(reservation -> reservation.getItem().getId())
                .collect(Collectors.toList());

        return allItems.stream()
                .filter(item -> !reservedItemIds.contains(item.getId()))
                .collect(Collectors.toList());
    }

    public Item updateItem(Long id, Item updatedItem) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item not found"));

        item.setName(updatedItem.getName());
        item.setDescription(updatedItem.getDescription());
        item.setItemType(updatedItem.getItemType());

        return itemRepository.save(item);
    }

    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }
}
