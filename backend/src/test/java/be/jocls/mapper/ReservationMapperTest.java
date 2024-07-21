package be.jocls.mapper;

import be.jocls.application.dto.ReservationDTO;
import be.jocls.domain.model.*;
import be.jocls.infrastructure.mapper.ReservationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReservationMapperTest {


    @Autowired
    private ReservationMapper reservationMapper;

    @BeforeEach
    public void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        reservationMapper = new ReservationMapper(modelMapper);
    }

    @Test
    public void testToEntity() {

        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .userRole(UserRole.STUDENT)
                .build();

        Item item = Item.builder()
                .name("Laptop")
                .itemType(ItemType.EQUIPMENT)
                .description("Lenovo laptop")
                .build();

        ReservationDTO reservationDTO = ReservationDTO.builder()
                .user(user)
                .item(item)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.APPROVED)
                .build();

        Reservation reservation = reservationMapper.toEntity(reservationDTO);

        assertEquals(reservationDTO.getUser(), reservation.getUser());
        assertEquals(reservationDTO.getItem(), reservation.getItem());
        assertEquals(reservationDTO.getStartTime(), reservation.getStartTime());
        assertEquals(reservationDTO.getEndTime(), reservation.getEndTime());
        assertEquals(reservationDTO.getReservationStatus(), reservation.getReservationStatus());
    }

    @Test
    public void testToDTO() {
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .userRole(UserRole.STUDENT)
                .build();

        Item item = Item.builder()
                .name("Laptop")
                .itemType(ItemType.EQUIPMENT)
                .description("Lenovo laptop")
                .build();

        Reservation reservation = Reservation.builder()
                .user(user)
                .item(item)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .reservationStatus(ReservationStatus.APPROVED)
                .build();

        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        assertEquals(reservation.getUser(), reservationDTO.getUser());
        assertEquals(reservation.getItem(), reservationDTO.getItem());
        assertEquals(reservation.getStartTime(), reservationDTO.getStartTime());
        assertEquals(reservation.getEndTime(), reservationDTO.getEndTime());
        assertEquals(reservation.getReservationStatus(), reservationDTO.getReservationStatus());
    }
}
