package com.project.repository;

import com.project.domain.entity.ReservationEntity;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends BaseRepository<ReservationEntity,Integer> {

    ReservationEntity findByIdAndDeletedValueFalse(Integer id);

    ReservationEntity softDelete(ReservationEntity entity);

    List<ReservationEntity> findReservationsByReaderId(Integer id);

    ReservationEntity findReservationsByReaderIdAndLocalDate(Integer id, LocalDate createdDate);

    List<ReservationEntity> findReservationsByLocalDate(LocalDate createdDate);

}
