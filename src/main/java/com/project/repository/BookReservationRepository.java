package com.project.repository;

import com.project.domain.entity.BookReservationEntity;

import java.time.LocalDate;
import java.util.List;

public interface BookReservationRepository extends BaseRepository<BookReservationEntity,Integer> {

    BookReservationEntity findByIdAndDeletedValueFalse(Integer id);

    BookReservationEntity softDelete(BookReservationEntity entity);

    List<BookReservationEntity> findBookReservationsByReservationId(Integer id);

    List<BookReservationEntity> findBookReservationsByReservationIdAndStatusNotReturned(Integer id);

    List<BookReservationEntity> findBookReservationsByBookId(Integer id);

    List<BookReservationEntity> findReservationsByIdAndLocalDate(Integer id, LocalDate createdDate);

    List<BookReservationEntity> findReservationsByLocalDate(LocalDate createdDate);

}
