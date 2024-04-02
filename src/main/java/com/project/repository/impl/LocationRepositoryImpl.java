package com.project.repository.impl;

import com.project.domain.entity.LocationEntity;
import com.project.domain.entity.PhysicalCopyEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.util.Constants;
import com.project.repository.LocationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepositoryImpl implements LocationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public LocationEntity save(LocationEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public LocationEntity update(LocationEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public LocationEntity findById(Integer id) {
        try{
            LocationEntity location = entityManager.createQuery(Constants.SELECT_LOCATION_BY_ID,
                            LocationEntity.class).setParameter("id",id).getSingleResult();
            if (location == null){
                throw new NoResultException("No results were found");
            }
            return location;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Location with id: " + id  + " can not be found.");
        }
    }

    @Transactional
    @Override
    public LocationEntity delete(LocationEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public LocationEntity findByShelfName(String nameOfTheShelf) {
        try {
            return entityManager.createQuery(Constants.SELECT_LOCATION_BY_SHELF_NAME, LocationEntity.class)
                    .setParameter("nameOfTheShelf", nameOfTheShelf).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<PhysicalCopyEntity> findBooksByLocationId(Integer id) {
        try{
            List<PhysicalCopyEntity> location = entityManager.createQuery(Constants.SELECT_BOOKS_BY_LOCATION_ID,
                            PhysicalCopyEntity.class).setParameter("id",id).getResultList();
            if (location.isEmpty()){
                throw new NoResultException("No results were found");
            }
            return location;
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<LocationEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<LocationEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_LOCATIONS,LocationEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
