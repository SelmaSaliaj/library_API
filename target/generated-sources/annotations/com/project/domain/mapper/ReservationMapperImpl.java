package com.project.domain.mapper;

import com.project.domain.dto.ReservationDTO;
import com.project.domain.entity.ReservationEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-15T06:45:38+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationDTO toReservationDTO(ReservationEntity reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationDTO reservationDTO = new ReservationDTO();

        reservationDTO.setReader( toReaderDTO( reservation.getReader() ) );
        reservationDTO.setId( reservation.getId() );
        reservationDTO.setCreatedDate( reservation.getCreatedDate() );
        reservationDTO.setLastModified( reservation.getLastModified() );
        reservationDTO.setDeleted( reservation.isDeleted() );

        return reservationDTO;
    }

    @Override
    public ReservationEntity toReservationEntity(ReservationDTO reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setReader( toReaderEntity( reservation.getReader() ) );
        reservationEntity.setId( reservation.getId() );
        reservationEntity.setCreatedDate( reservation.getCreatedDate() );
        reservationEntity.setLastModified( reservation.getLastModified() );
        reservationEntity.setDeleted( reservation.isDeleted() );

        return reservationEntity;
    }
}
