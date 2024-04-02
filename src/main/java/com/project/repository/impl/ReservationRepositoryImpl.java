package com.project.repository.impl;

import com.project.domain.entity.ReservationEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.repository.ReservationRepository;
import com.project.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public ReservationEntity save(ReservationEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public ReservationEntity update(ReservationEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public ReservationEntity findById(Integer id) {
        try{
            ReservationEntity reservation = entityManager
                    .createQuery(Constants.SELECT_RESERVATION_BY_ID, ReservationEntity.class)
                    .setParameter("id",id).getSingleResult();
            if (reservation == null){
                throw new NoResultException("No results were found");
            }
            return reservation;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Reservation with id: " + id  + " can not be found.");
        }
    }

    @Override
    public ReservationEntity delete(ReservationEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public ReservationEntity findByIdAndDeletedValueFalse(Integer id) {
        try{
            ReservationEntity reservation = entityManager
                    .createQuery(Constants.SELECT_RESERVATION_BY_ID_AND_DELETED_FALSE, ReservationEntity.class)
                    .setParameter("id",id).getSingleResult();
            if(reservation == null){
                throw new NoResultException("No results were found");
            }
            return reservation;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Reservation with id: " + id  +
                    " can not be found within the existing reservations.");
        }
    }

    @Override
    public ReservationEntity softDelete(ReservationEntity entity) {
        entity.setDeleted(true);
        return update(entity);
    }

    @Override
    public List<ReservationEntity> findReservationsByReaderId(Integer id) {
        try{
            List<ReservationEntity> reservations = entityManager
                    .createQuery(Constants.SELECT_RESERVATIONS_BY_READER_ID, ReservationEntity.class)
                    .setParameter("id",id).getResultList();
            if (reservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return reservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public ReservationEntity findReservationsByReaderIdAndLocalDate(Integer id, LocalDate createdDate) {
        try{
            ReservationEntity reservation = entityManager
                    .createQuery(Constants.SELECT_RESERVATION_BY_READER_ID_CREATED_DATE, ReservationEntity.class)
                    .setParameter("id",id).setParameter("createdDate",createdDate)
                    .getSingleResult();
            if(reservation == null){
                throw new NoResultException("No results were found");
            }
            return reservation;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Reservation with reader id: "+ id +
                    " and created date: " + createdDate + " does not exist.");
        }
    }

    @Override
    public List<ReservationEntity> findReservationsByLocalDate(LocalDate createdDate) {
        try{
            List<ReservationEntity> reservations = entityManager
                    .createQuery(Constants.SELECT_RESERVATION_BY_CREATED_DATE, ReservationEntity.class)
                    .setParameter("createdDate",createdDate).getResultList();
            if(reservations.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return reservations;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<ReservationEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<ReservationEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_RESERVATIONS,ReservationEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
