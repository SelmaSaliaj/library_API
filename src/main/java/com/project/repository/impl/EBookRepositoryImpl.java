package com.project.repository.impl;

import com.project.domain.entity.EBookEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.util.Constants;
import com.project.repository.EBookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EBookRepositoryImpl implements EBookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public EBookEntity save(EBookEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public EBookEntity update(EBookEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public EBookEntity findById(Integer id) {
        try {
            EBookEntity eBook = entityManager.createQuery(Constants.SELECT_EBOOK_BY_ID,EBookEntity.class)
                    .setParameter("id",id).getSingleResult();
            if (eBook == null){
                throw new NoResultException("No results were found");
            }
            return eBook;
        }catch (NoResultException e){
            throw new ResourceNotFoundException("EBook with id: " + id  + " can not be found.");
        }
    }

    @Transactional
    @Override
    public EBookEntity delete(EBookEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    @Override
    public List<EBookEntity> findByTitle(String title) {
        List<EBookEntity> eBooks = entityManager.createQuery(Constants.SELECT_EBOOKS_BY_TITLE,
                            EBookEntity.class).setParameter("title",title).getResultList();
        if (eBooks.isEmpty()){
            throw new ResourceNotFoundException("There are no ebooks titled: " + title);
        }
        return eBooks;
    }

    @Override
    public List<EBookEntity> findByAuthor(String author) {
        List<EBookEntity> eBooks = entityManager.createQuery(Constants.SELECT_EBOOKS_BY_AUTHOR,
                            EBookEntity.class).setParameter("author",author).getResultList();
        if (eBooks.isEmpty()){
            throw new ResourceNotFoundException("There are no ebooks by author: " + author);
        }
        return eBooks;
    }

    @Override
    public EBookEntity findByTitleAndAuthor(String title, String author) {
        try {
            return entityManager.createQuery(Constants.SELECT_EBOOKS_BY_TITLE_AND_AUTHOR, EBookEntity.class)
                    .setParameter("author", author).setParameter("title", title).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<EBookEntity> getAll(int pageNumber, int pageSize) {
        TypedQuery<EBookEntity> findQuery = entityManager.createQuery(Constants.SELECT_ALL_EBOOK,EBookEntity.class);
        findQuery.setFirstResult((pageNumber - 1) * pageSize);
        findQuery.setMaxResults(pageSize);
        return findQuery.getResultList();
    }

}
