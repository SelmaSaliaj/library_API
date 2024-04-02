package com.project.service.impl;

import com.project.domain.dto.EBookDTO;
import com.project.domain.dto.EBookRequest;
import com.project.domain.entity.EBookEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.domain.mapper.EBookMapper;
import com.project.repository.EBookRepository;
import com.project.service.EBookService;
import com.project.util.ValidationUtils;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EBookServiceImpl implements EBookService {

    private final EBookRepository repository;

    @Autowired
    public EBookServiceImpl(EBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public EBookDTO findById(Integer id){
        return EBookMapper.toDTO(repository.findById(id));
    }

    @Transactional
    @Override
    public EBookDTO save(EBookRequest request) {

        request.setTitle(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getTitle(), "title"));
        request.setAuthor(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getAuthor(), "author"));

        ValidationUtils.checkIfEBookWithGivenTitleAndAuthorAlreadyExists(request,repository);

        return EBookMapper.toDTO(repository.save(EBookMapper.toEntity(request)));

    }

    @Transactional
    @Override
    public EBookDTO update(Integer id, EBookRequest request){

        findById(id);

        request.setTitle(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getTitle(), "title"));
        request.setAuthor(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getAuthor(), "author"));

        ValidationUtils.checkIfEBookWithGivenTitleAndAuthorAlreadyExists(id, request, repository);

        EBookDTO eBook = EBookMapper.toDTO(EBookMapper.toEntity(request));
        eBook.setId(id);

        return EBookMapper.toDTO(repository.update(EBookMapper.toEntity(eBook)));
    }

    @Transactional
    @Override
    public EBookDTO delete(Integer id){
            findById(id);
            return EBookMapper.toDTO(repository.delete(repository.findById(id)));
    }

    @Override
    public List<EBookDTO> findByTitle(String title) {

        title = ValidationUtils.checkIfValueIsNotCorrectlyValidated(title, "title");

        List<EBookEntity> list = repository.findByTitle(title);

        return list.stream()
                .map(EBookMapper::toDTO)
                .toList();
    }

    @Override
    public List<EBookDTO> findByAuthor(String author) {

        author = ValidationUtils.checkIfValueIsNotCorrectlyValidated(author, "author");

        List<EBookEntity> list = repository.findByAuthor(author);

        return list.stream()
                .map(EBookMapper::toDTO)
                .toList();
    }

    @Override
    public EBookDTO findByTitleAndAuthor(String title, String author) {
        try {
            title = ValidationUtils.checkIfValueIsNotCorrectlyValidated(title, "title");
            author = ValidationUtils.checkIfValueIsNotCorrectlyValidated(author, "author");

            EBookDTO eBook = EBookMapper.toDTO(repository.findByTitleAndAuthor(title,author));
            if (eBook == null){
                throw new NoResultException("No results were found");
            }
            return eBook;

        } catch (NoResultException e){
            throw new ResourceNotFoundException("EBook with title: " + title  + " and author: " +
                    author + " can not be found.");
        }
    }

    @Override
    public List<EBookDTO> getAllEBooks(int pageNumber, int pageSize) {
        List<EBookEntity> list = repository.getAll(pageNumber,pageSize);
        return list.stream()
                .map(EBookMapper::toDTO)
                .collect(Collectors.toList());
    }

}
