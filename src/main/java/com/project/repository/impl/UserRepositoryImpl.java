package com.project.repository.impl;

import com.project.domain.entity.UserEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.repository.UserRepository;
import com.project.util.Constants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public UserEntity save(UserEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public UserEntity update(UserEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public UserEntity findById(Integer id) {
        try{
            UserEntity user = entityManager.createQuery(Constants.SELECT_USER_BY_ID,UserEntity.class)
                    .setParameter("id",id).getSingleResult();
            if(user == null){
                throw new NoResultException("Not found");
            }
            return user;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("User with id: " + id + " doesn't exist");
        }
    }

    @Transactional
    @Override
    public UserEntity delete(UserEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public UserEntity findByUsername(String username) {
        try{
            UserEntity user = entityManager.createQuery(Constants.SELECT_USER_BY_USERNAME,UserEntity.class)
                    .setParameter("username",username).getSingleResult();
            if(user == null){
                throw new NoResultException("Not found");
            }
            return user;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<UserEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<UserEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_USER,UserEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
