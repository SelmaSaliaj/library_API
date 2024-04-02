package com.project.service.impl;

import com.project.domain.dto.BookReservationDTO;
import com.project.domain.dto.BookReservationRequest;
import com.project.domain.dto.PhysicalCopyDTO;
import com.project.domain.dto.ReservationDTO;
import com.project.domain.entity.BookReservationEntity;
import com.project.domain.enums.BookReservationStatus;
import com.project.domain.exception.MethodCanNotBePerformedException;
import com.project.domain.exception.ResourceAlreadyExistsException;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.domain.exception.ValueNotSupportedException;
import com.project.repository.BookReservationRepository;
import com.project.service.BookReservationService;
import com.project.service.PhysicalCopyService;
import com.project.service.ReservationService;
import com.project.util.Constants;
import com.project.util.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookReservationServiceImpl implements BookReservationService {

    private final BookReservationRepository repository;
    private final ReservationService reservationService;
    private final PhysicalCopyService bookService;

    @Autowired
    public BookReservationServiceImpl(BookReservationRepository repository, ReservationService reservationService,
                                      PhysicalCopyService bookService) {
        this.repository = repository;
        this.reservationService = reservationService;
        this.bookService = bookService;
    }

    @Override
    public BookReservationDTO findById(Integer id) {
        return Constants.BOOK_RESERVATION_MAPPER.toBookReservationDTO(repository.findById(id));
    }

    @Override
    public BookReservationDTO findByIdAndDeletedValueFalse(Integer id) {
        return Constants.BOOK_RESERVATION_MAPPER.toBookReservationDTO(repository.findByIdAndDeletedValueFalse(id));
    }

    @Transactional
    @Override
    public void save(Integer readerId, BookReservationRequest request) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getReturnedDate(),currentDateTime,20);

        ReservationDTO reservation = reservationService.findReservationsByReaderIdAndLocalDate(readerId, currentDateTime);
        PhysicalCopyDTO book = bookService.findByTitleAndAuthor(request.getTitle(), request.getAuthor());

        List<BookReservationEntity> bookReservations = repository.findBookReservationsByReservationId(reservation.getId());
        if(bookReservations != null){
            for (BookReservationEntity bookReservation: bookReservations) {
                if(Objects.equals(bookReservation.getBook().getId(), book.getId())){
                    throw new ResourceAlreadyExistsException("The book reservation for book with " +
                            "title: " + book.getTitle() + " and author: " + book.getAuthor() +
                            " already exists in reservation with id: " + reservation.getId());
                }
            }
        }

        List<ReservationDTO> reservationDTOList = reservationService.findReservationsByReaderId(readerId);
        for (ReservationDTO list: reservationDTOList) {
            List<BookReservationEntity> bookReservationDTOS = repository.findBookReservationsByReservationIdAndStatusNotReturned(list.getId());
            if(bookReservationDTOS != null){
                for (BookReservationEntity bookReservationEntity: bookReservationDTOS) {
                    if(Objects.equals(bookReservationEntity.getBook().getId(), book.getId())){
                        throw new MethodCanNotBePerformedException("You have reserved this book before and have not returned it.");
                    }
                }
            }
        }

        if (book.getNumberOfCopiesAvailable() == 0){
            throw new ValueNotSupportedException("Book with title: " + book.getTitle() + " and author: " +
                   book.getAuthor() + " has no copies available to book.");
        }
        int newNumberOfAvailableCopies = book.getNumberOfCopiesAvailable() - 1;
        book.setNumberOfCopiesAvailable(newNumberOfAvailableCopies);
        book = bookService.update(book);

        reservation.setLastModified(currentDateTime);
        reservation = reservationService.update(reservation);

        BookReservationDTO bookReservation = new BookReservationDTO();
        bookReservation.setCreatedDate(currentDateTime);
        bookReservation.setLastModified(currentDateTime);
        bookReservation.setReturnedDate(request.getReturnedDate());
        bookReservation.setStatus(BookReservationStatus.RESERVED);
        bookReservation.setChanged(0);
        bookReservation.setDeleted(false);
        bookReservation.setReservation(reservation);
        bookReservation.setBook(book);

        Constants.BOOK_RESERVATION_MAPPER.toBookReservationDTO(repository
                .save(Constants.BOOK_RESERVATION_MAPPER.toBookReservationEntity(bookReservation)));

    }

    @Transactional
    @Override
    public void update(Integer id, LocalDateTime newToReturnDateTime) {

        BookReservationDTO bookReservation = findByIdAndDeletedValueFalse(id);

        ValidationUtils.checkIfValueIsNotCorrectlyValidated(newToReturnDateTime,
                bookReservation.getReturnedDate(),7);

        if (bookReservation.getChanged() == 3){
            throw new MethodCanNotBePerformedException("Book reservation can not be updated anymore.");
        }
        int newChangedValue = bookReservation.getChanged() + 1;
        bookReservation.setChanged(newChangedValue);

        LocalDateTime currentDateTime = LocalDateTime.now();

        ReservationDTO reservation = bookReservation.getReservation();
        reservation.setLastModified(currentDateTime);
        reservation = reservationService.update(reservation);

        bookReservation.setLastModified(currentDateTime);
        bookReservation.setReturnedDate(newToReturnDateTime);
        bookReservation.setStatus(BookReservationStatus.POSTPONED);
        bookReservation.setReservation(reservation);

        Constants.BOOK_RESERVATION_MAPPER.toBookReservationDTO(repository
                .update(Constants.BOOK_RESERVATION_MAPPER.toBookReservationEntity(bookReservation)));
}

    @Transactional
    @Override
    public void update(BookReservationDTO bookReservation) {
        Constants.BOOK_RESERVATION_MAPPER.toBookReservationDTO(repository
                .update(Constants.BOOK_RESERVATION_MAPPER.toBookReservationEntity(bookReservation)));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        BookReservationDTO bookReservation = findByIdAndDeletedValueFalse(id);
        if(bookReservation.getStatus() != BookReservationStatus.RETURNED){
            throw new MethodCanNotBePerformedException("Book reservation with id: " + id +
                    " can not be deleted, since the book has not been returned");
        }
        Constants.BOOK_RESERVATION_MAPPER.toBookReservationDTO(repository
                .softDelete(Constants.BOOK_RESERVATION_MAPPER.toBookReservationEntity(bookReservation)));

    }

    @Transactional
    @Override
    public void updateStatusToReturned(Integer id) {
        BookReservationDTO bookReservation = findByIdAndDeletedValueFalse(id);
        if(bookReservation.getStatus() == BookReservationStatus.RETURNED){
            throw new MethodCanNotBePerformedException("Book reservation with id: " + id +
                    " has already been returned");
        }
        PhysicalCopyDTO book = bookReservation.getBook();
        int newNumberOfAvailableCopies = book.getNumberOfCopiesAvailable() + 1;
        book.setNumberOfCopiesAvailable(newNumberOfAvailableCopies);
        book = bookService.update(book);

        LocalDateTime currentDateTime = LocalDateTime.now();

        ReservationDTO reservation = bookReservation.getReservation();
        reservation.setLastModified(currentDateTime);
        reservation = reservationService.update(reservation);

        bookReservation.setLastModified(currentDateTime);
        bookReservation.setStatus(BookReservationStatus.RETURNED);
        bookReservation.setBook(book);
        bookReservation.setReservation(reservation);
        update(bookReservation);
    }

    @Override
    public List<BookReservationDTO> findBookReservationsByReservationId(Integer id) {
        reservationService.findByIdAndDeletedValueFalse(id);
        List<BookReservationEntity> bookReservations = repository.findBookReservationsByReservationId(id);
        if (bookReservations == null){
            return null;
        }
        return bookReservations.stream()
                .map(Constants.BOOK_RESERVATION_MAPPER::toBookReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReservationDTO> findBookReservationsByReservationIdAndStatusNotReturned(Integer id) {
        reservationService.findByIdAndDeletedValueFalse(id);
        List<BookReservationEntity> bookReservations = repository
                .findBookReservationsByReservationIdAndStatusNotReturned(id);
        if (bookReservations == null){
            throw new ResourceNotFoundException("All book reservations for reservation with id: "
                    + id + " are returned.");
        }
        return bookReservations.stream()
                .map(Constants.BOOK_RESERVATION_MAPPER::toBookReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReservationDTO> findBookReservationsByBookId(Integer id) {
        bookService.findById(id);
        List<BookReservationEntity> bookReservations = repository.findBookReservationsByBookId(id);
        if (bookReservations == null){
            throw new ResourceNotFoundException("There are no book reservations for book with id: "
                    + id);
        }
        return bookReservations.stream()
                .map(Constants.BOOK_RESERVATION_MAPPER::toBookReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReservationDTO> findBookReservationsByIdAndLocalDate(Integer id, LocalDateTime createdDate) {
        repository.findByIdAndDeletedValueFalse(id);
        List<BookReservationEntity> bookReservations = repository.findReservationsByIdAndLocalDate(id,createdDate.toLocalDate());
        if (bookReservations == null){
            throw new ResourceNotFoundException("There are no book reservations with id: " + id +
                    " and created date: " + createdDate);
        }
        return bookReservations.stream()
                .map(Constants.BOOK_RESERVATION_MAPPER::toBookReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReservationDTO> findBookReservationsByLocalDate(LocalDateTime createdDate) {
        List<BookReservationEntity> bookReservations = repository.findReservationsByLocalDate(createdDate.toLocalDate());
        if (bookReservations == null){
            throw new ResourceNotFoundException("There are no book reservations created on date: " + createdDate);
        }
        return bookReservations.stream()
                .map(Constants.BOOK_RESERVATION_MAPPER::toBookReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookReservationDTO> getAllBookReservations(int pageNumber, int pageSize) {
        List<BookReservationEntity> list = repository.getAll(pageNumber,pageSize);
        return list.stream()
                .map(Constants.BOOK_RESERVATION_MAPPER::toBookReservationDTO)
                .collect(Collectors.toList());
    }

}
