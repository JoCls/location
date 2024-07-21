package be.jocls.infrastructure.mapper;

import be.jocls.application.dto.ReservationDTO;
import be.jocls.domain.model.Reservation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ReservationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReservationDTO toDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    public Reservation toEntity(ReservationDTO reservationDTO) {
        return modelMapper.map(reservationDTO, Reservation.class);
    }
}
