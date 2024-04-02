package com.project.repository.impl;

import com.project.domain.entity.ReaderEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.util.Constants;
import com.project.repository.ReaderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReaderRepositoryImpl implements ReaderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ReaderEntity save(ReaderEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public ReaderEntity update(ReaderEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public ReaderEntity findById(Integer id) {
        try{
            ReaderEntity reader = entityManager.createQuery(Constants.SELECT_READER_BY_ID,ReaderEntity.class)
                    .setParameter("id",id).getSingleResult();
            if(reader == null){
                throw new NoResultException("No results were found");
            }
            return reader;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Reader with id: " + id  + " can not be found.");
        }
    }

    @Override
    public ReaderEntity delete(ReaderEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public ReaderEntity findByIdAndDeletedValueFalse(Integer id) {
        try{
            ReaderEntity reader = entityManager.createQuery(Constants.SELECT_READER_BY_ID_AND_DELETED_FALSE,
                            ReaderEntity.class).setParameter("id",id).getSingleResult();
            if(reader == null){
                throw new NoResultException("No results were found");
            }
            return reader;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Reader with id: " + id  +
                    " can not be found within the existing readers.");
        }
    }

    @Override
    public ReaderEntity softDelete(ReaderEntity entity) {
        entity.setDeleted(true);
        return update(entity);
    }

    @Override
    public ReaderEntity findByEmail(String email) {
        try{
            ReaderEntity reader = entityManager.createQuery(Constants.SELECT_READER_BY_EMAIL,ReaderEntity.class)
                    .setParameter("email",email).getSingleResult();
            if(reader == null){
                throw new NoResultException("No results were found");
            }
            return reader;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<ReaderEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<ReaderEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_READER,ReaderEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
