package com.project.domain.mapper;

import com.project.domain.dto.BookReservationDTO;
import com.project.domain.entity.BookReservationEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-15T06:45:38+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class BookReservationMapperImpl implements BookReservationMapper {

    @Override
    public BookReservationDTO toBookReservationDTO(BookReservationEntity reservation) {
        if ( reservation == null ) {
            return null;
        }

        BookReservationDTO bookReservationDTO = new BookReservationDTO();

        bookReservationDTO.setReservation( toReservationDTO( reservation.getReservation() ) );
        bookReservationDTO.setBook( toPhysicalCopyDTO( reservation.getBook() ) );
        bookReservationDTO.setId( reservation.getId() );
        bookReservationDTO.setCreatedDate( reservation.getCreatedDate() );
        bookReservationDTO.setReturnedDate( reservation.getReturnedDate() );
        bookReservationDTO.setLastModified( reservation.getLastModified() );
        bookReservationDTO.setStatus( reservation.getStatus() );
        bookReservationDTO.setDeleted( reservation.isDeleted() );
        bookReservationDTO.setChanged( reservation.getChanged() );

        return bookReservationDTO;
    }

    @Override
    public BookReservationEntity toBookReservationEntity(BookReservationDTO reservation) {
        if ( reservation == null ) {
            return null;
        }

        BookReservationEntity bookReservationEntity = new BookReservationEntity();

        bookReservationEntity.setReservation( toReservationEntity( reservation.getReservation() ) );
        bookReservationEntity.setBook( toPhysicalCopyEntity( reservation.getBook() ) );
        bookReservationEntity.setId( reservation.getId() );
        bookReservationEntity.setCreatedDate( reservation.getCreatedDate() );
        bookReservationEntity.setLastModified( reservation.getLastModified() );
        bookReservationEntity.setReturnedDate( reservation.getReturnedDate() );
        bookReservationEntity.setDeleted( reservation.isDeleted() );
        bookReservationEntity.setChanged( reservation.getChanged() );
        bookReservationEntity.setStatus( reservation.getStatus() );

        return bookReservationEntity;
    }
}
