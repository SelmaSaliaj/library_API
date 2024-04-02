package com.project.domain.mapper;

import com.project.domain.dto.*;
import com.project.domain.entity.BookReservationEntity;
import com.project.domain.entity.PhysicalCopyEntity;
import com.project.domain.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookReservationMapper {

    ReservationMapper RESERVATION_MAPPER = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "reservation",target = "reservation",qualifiedByName = "toReservationDTO")
    @Mapping(source = "book",target = "book",qualifiedByName = "toPhysicalCopyDTO")
    BookReservationDTO toBookReservationDTO(BookReservationEntity reservation);

    @Mapping(source = "reservation",target = "reservation",qualifiedByName = "toReservationEntity")
    @Mapping(source = "book",target = "book",qualifiedByName = "toPhysicalCopyEntity")
    BookReservationEntity toBookReservationEntity(BookReservationDTO reservation);

    @Named("toPhysicalCopyDTO")
    default PhysicalCopyDTO toPhysicalCopyDTO(PhysicalCopyEntity book){
        return PhysicalCopyMapper.toDTO(book);
    }

    @Named("toPhysicalCopyEntity")
    default PhysicalCopyEntity toPhysicalCopyEntity(PhysicalCopyDTO book){
        return PhysicalCopyMapper.toEntity(book);
    }

    @Named("toReservationDTO")
    default ReservationDTO toReservationDTO(ReservationEntity reservation){
        return RESERVATION_MAPPER.toReservationDTO(reservation);
    }

    @Named("toReservationEntity")
    default ReservationEntity toReservationEntity(ReservationDTO reservation){
        return RESERVATION_MAPPER.toReservationEntity(reservation);
    }

}
