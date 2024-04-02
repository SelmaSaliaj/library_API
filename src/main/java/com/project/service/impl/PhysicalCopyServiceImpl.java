package com.project.service.impl;

import com.project.domain.dto.PhysicalCopyDTO;
import com.project.domain.dto.PhysicalCopyRequest;
import com.project.domain.entity.LocationEntity;
import com.project.domain.entity.PhysicalCopyEntity;
import com.project.domain.exception.MethodCanNotBePerformedException;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.domain.mapper.PhysicalCopyMapper;
import com.project.repository.BookReservationRepository;
import com.project.repository.LocationRepository;
import com.project.repository.PhysicalCopyRepository;
import com.project.service.PhysicalCopyService;
import com.project.util.ValidationUtils;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhysicalCopyServiceImpl implements PhysicalCopyService {

    private final PhysicalCopyRepository repository;
    private final LocationRepository locationRepository;
    private final BookReservationRepository bookReservationRepository;

    @Autowired
    public PhysicalCopyServiceImpl(PhysicalCopyRepository repository, LocationRepository locationRepository,
                                   BookReservationRepository bookReservationRepository) {
        this.repository = repository;
        this.locationRepository = locationRepository;
        this.bookReservationRepository = bookReservationRepository;
    }

    @Override
    public PhysicalCopyDTO findById(Integer id) {
        return PhysicalCopyMapper.toDTO(repository.findById(id));
    }

    @Transactional
    @Override
    public PhysicalCopyDTO save(PhysicalCopyRequest request) {

        request.setTitle(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getTitle(), "title"));
        request.setAuthor(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getAuthor(), "author"));
        ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getNumberOfCopies());

        ValidationUtils.checkIfBookWithGivenTitleAndAuthorAlreadyExists(request, repository);

        LocationEntity location = ValidationUtils.getTheLocationObject(request, locationRepository);

        return PhysicalCopyMapper.toDTO(repository.save(PhysicalCopyMapper.toEntity(request,location)));

    }

    @Transactional
    @Override
    public PhysicalCopyDTO update(Integer id, PhysicalCopyRequest request) {

        PhysicalCopyDTO book = findById(id);

        request.setTitle(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getTitle(), "title"));
        request.setAuthor(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getAuthor(), "author"));
        ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getNumberOfCopies());

        if(ValidationUtils.checkIfBookWithGivenTitleAndAuthorAlreadyExists(id, request, repository) == null) {
            ValidationUtils.checkForOnGoingReservations(book, request);
        }

        int newValueOfAvailableCopies = ValidationUtils.getNewValueOfAvailableCopies(request, book);

        LocationEntity location = ValidationUtils.getTheLocationObject(request, locationRepository);

        book = PhysicalCopyMapper.toDTO(PhysicalCopyMapper.toEntity(request,location));

        if (book == null){
            throw new AssertionError("Trouble mapping the object, it received null value");
        }

        book.setNumberOfCopiesAvailable(newValueOfAvailableCopies);
        book.setId(id);

        return PhysicalCopyMapper.toDTO(repository.update(PhysicalCopyMapper.toEntity(book)));

    }

    @Override
    public PhysicalCopyDTO update(PhysicalCopyDTO book) {
        return PhysicalCopyMapper.toDTO(repository.update(PhysicalCopyMapper.toEntity(book)));
    }

    @Transactional
    @Override
    public PhysicalCopyDTO delete(Integer id) {

        PhysicalCopyDTO book = findById(id);

        ValidationUtils.checkForBookAccessibility(id,book);

        if(ValidationUtils.isNotReserved(book)){
            if(ValidationUtils.areThereAnyBookReservationsForTheGivenBook(id,bookReservationRepository)){
                book.setNumberOfCopies(0);
                book.setNumberOfCopiesAvailable(0);
                book.setLocation(null);
                return PhysicalCopyMapper.toDTO(repository.update(PhysicalCopyMapper.toEntity(book)));
            }
            return PhysicalCopyMapper.toDTO(repository.delete(repository.findById(id)));
        } else {
            throw new MethodCanNotBePerformedException("Book with id:" + id + " can not be deleted, " +
                    "there are still on going reservations");
        }
    }

    @Override
    public List<PhysicalCopyDTO> findByTitle(String title) {

        title = ValidationUtils.checkIfValueIsNotCorrectlyValidated(title, "title");

        List<PhysicalCopyEntity> list = repository.findByTitle(title);

        return list.stream()
                .map(PhysicalCopyMapper::toDTO)
                .toList();
    }

    @Override
    public List<PhysicalCopyDTO> findByAuthor(String author) {

        author = ValidationUtils.checkIfValueIsNotCorrectlyValidated(author, "author");

        List<PhysicalCopyEntity> list = repository.findByAuthor(author);

        return list.stream()
                .map(PhysicalCopyMapper::toDTO)
                .toList();
    }

    @Override
    public PhysicalCopyDTO findByTitleAndAuthor(String title, String author) {
        try {
            title = ValidationUtils.checkIfValueIsNotCorrectlyValidated(title, "title");
            author = ValidationUtils.checkIfValueIsNotCorrectlyValidated(author, "author");

            PhysicalCopyDTO book = PhysicalCopyMapper.toDTO(repository.findByTitleAndAuthor(title,author));
            if (book == null){
                throw new NoResultException("No results were found");
            }
            return book;

        } catch (NoResultException e){
            throw new ResourceNotFoundException("Book with title: " + title  + " and author: " +
                    author + " can not be found.");
        }
    }

    @Override
    public List<PhysicalCopyDTO> getAllPhysicalBooks(int pageNumber, int pageSize) {
        List<PhysicalCopyEntity> list = repository.getAll(pageNumber,pageSize);
        return list.stream()
                .map(PhysicalCopyMapper::toDTO)
                .collect(Collectors.toList());
    }


}
