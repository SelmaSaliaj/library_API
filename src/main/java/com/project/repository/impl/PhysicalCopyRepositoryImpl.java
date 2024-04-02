package com.project.repository.impl;

import com.project.domain.entity.PhysicalCopyEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.util.Constants;
import com.project.repository.PhysicalCopyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhysicalCopyRepositoryImpl implements PhysicalCopyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public PhysicalCopyEntity save(PhysicalCopyEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public PhysicalCopyEntity update(PhysicalCopyEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public PhysicalCopyEntity findById(Integer id) {
        try {
            PhysicalCopyEntity book = entityManager.createQuery(Constants.SELECT_BOOK_BY_ID,
                            PhysicalCopyEntity.class).setParameter("id",id).getSingleResult();
            if (book == null){
                throw new NoResultException("No results were found");
            }
            return book;
        } catch (NoResultException e){
            throw new ResourceNotFoundException("Book with id: " + id  + " can not be found.");
        }
    }

    @Transactional
    @Override
    public PhysicalCopyEntity delete(PhysicalCopyEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public List<PhysicalCopyEntity> findByTitle(String title) {
        List<PhysicalCopyEntity> books = entityManager.createQuery(Constants.SELECT_BOOKS_BY_TITLE,
                        PhysicalCopyEntity.class).setParameter("title", title).getResultList();
        if (books.isEmpty()){
            throw new ResourceNotFoundException("There are no books titled: " + title);
        }
        return books;
    }

    @Override
    public List<PhysicalCopyEntity> findByAuthor(String author) {
        List<PhysicalCopyEntity> books = entityManager.createQuery(Constants.SELECT_BOOKS_BY_AUTHOR,
                        PhysicalCopyEntity.class).setParameter("author", author).getResultList();
        if (books.isEmpty()){
            throw new ResourceNotFoundException("There are no books by author: " + author);
        }
        return books;
    }

    @Override
    public PhysicalCopyEntity findByTitleAndAuthor(String title, String author) {
        try {
            return entityManager.createQuery(Constants.SELECT_BOOKS_BY_TITLE_AND_AUTHOR,
                            PhysicalCopyEntity.class).setParameter("title", title)
                            .setParameter("author", author).getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<PhysicalCopyEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<PhysicalCopyEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_BOOKS,PhysicalCopyEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
