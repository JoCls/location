package be.jocls.database;


import be.jocls.domain.model.*;
import be.jocls.domain.repository.ItemRepository;
import be.jocls.domain.repository.ReservationRepository;
import be.jocls.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Rollback
public class DBIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testInsertAndRetrieveUser() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("testuser@example.com")
                .userRole(UserRole.STUDENT)
                .build();
        userRepository.save(user);

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertEquals("testuser", retrievedUser.getUsername());
    }

    @Test
    public void testInsertAndRetrieveItem() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("testuser@example.com")
                .userRole(UserRole.STUDENT)
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Projector")
                .itemType(ItemType.EQUIPMENT)
                .description("A high-quality projector")
                .build();
        itemRepository.save(item);

        Item retrievedItem = itemRepository.findById(item.getId()).orElse(null);
        assertEquals("Projector", retrievedItem.getName());
    }

    @Test
    public void testInsertAndRetrieveReservation() {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .email("testuser@example.com")
                .userRole(UserRole.STUDENT)
                .build();
        userRepository.save(user);

        Item item = Item.builder()
                .name("Projector")
                .itemType(ItemType.EQUIPMENT)
                .description("A high-quality projector")
                .build();
        itemRepository.save(item);

        Reservation reservation = Reservation.builder()
                .user(user)
                .item(item)
                .startTime(LocalDateTime.of(2024, 7, 18, 8, 0))
                .endTime(LocalDateTime.of(2024, 7, 18, 10, 0))
                .reservationStatus(ReservationStatus.APPROVED)
                .build();
        reservationRepository.save(reservation);

        Reservation retrievedReservation = reservationRepository.findById(reservation.getId()).orElse(null);
        assertEquals(LocalDateTime.of(2024, 7, 18, 8, 0), retrievedReservation.getStartTime());
    }
}