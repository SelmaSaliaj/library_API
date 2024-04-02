package com.project.repository.impl;

import com.project.domain.entity.BookReservationEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.repository.BookReservationRepository;
import com.project.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BookReservationRepositoryImpl implements BookReservationRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public BookReservationEntity save(BookReservationEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public BookReservationEntity update(BookReservationEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public BookReservationEntity findById(Integer id) {
        return entityManager.createQuery(Constants.SELECT_BOOK_RESERVATION_BY_ID,BookReservationEntity.class)
                .setParameter("id",id).getSingleResult();
    }

    @Override
    public BookReservationEntity delete(BookReservationEntity entity) {
        entity = softDelete(entity);
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public BookReservationEntity findByIdAndDeletedValueFalse(Integer id) {
        try{
            BookReservationEntity bookReservation = entityManager.createQuery(Constants.SELECT_BOOK_RESERVATION_BY_ID_AND_DELETED_FALSE,
                    BookReservationEntity.class).setParameter("id",id).getSingleResult();
            if(bookReservation == null){
                throw new NoResultException("No results were found");
            }
            return bookReservation;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Book reservation with id: " + id  +
                    " can not be found within the existing book reservations.");
        }
    }

    @Override
    public BookReservationEntity softDelete(BookReservationEntity entity) {
        entity.setDeleted(true);
        return update(entity);
    }

    @Override
    public List<BookReservationEntity> findBookReservationsByReservationId(Integer id) {
        try{
            List<BookReservationEntity> bookReservations = entityManager
                    .createQuery(Constants.SELECT_BOOK_RESERVATION_BY_RESERVATION_ID, BookReservationEntity.class)
                    .setParameter("id",id).getResultList();
            if (bookReservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return bookReservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<BookReservationEntity> findBookReservationsByReservationIdAndStatusNotReturned(Integer id) {
        try{
            List<BookReservationEntity> bookReservations = entityManager
                    .createQuery(Constants.SELECT_BOOK_RESERVATION_BY_RESERVATION_ID_AND_STATUS_NOT_RETURNED,
                    BookReservationEntity.class).setParameter("id",id).getResultList();
            if (bookReservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return bookReservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<BookReservationEntity> findBookReservationsByBookId(Integer id) {
        try{
            List<BookReservationEntity> bookReservations = entityManager
                    .createQuery(Constants.SELECT_BOOK_RESERVATION_BY_BOOK_ID,
                            BookReservationEntity.class).setParameter("id",id).getResultList();
            if (bookReservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return bookReservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<BookReservationEntity> findReservationsByIdAndLocalDate(Integer id, LocalDate createdDate) {
        try{
            List<BookReservationEntity> bookReservations = entityManager
                    .createQuery(Constants.SELECT_BOOK_RESERVATION_BY_ID_AND_CREATED_DATE, BookReservationEntity.class)
                    .setParameter("id",id).setParameter("createdDate",createdDate).getResultList();
            if(bookReservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return bookReservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<BookReservationEntity> findReservationsByLocalDate(LocalDate createdDate) {
        try{
            List<BookReservationEntity> bookReservations = entityManager
                    .createQuery(Constants.SELECT_BOOK_RESERVATION_BY_CREATED_DATE, BookReservationEntity.class)
                    .setParameter("createdDate",createdDate).getResultList();
            if(bookReservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return bookReservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<BookReservationEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<BookReservationEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_BOOK_RESERVATIONS,BookReservationEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
