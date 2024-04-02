package com.project.util;

import com.project.domain.dto.*;
import com.project.domain.entity.BookReservationEntity;
import com.project.domain.entity.LocationEntity;
import com.project.domain.entity.ReservationEntity;
import com.project.domain.exception.MethodCanNotBePerformedException;
import com.project.domain.exception.ResourceAlreadyExistsException;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.domain.exception.ValueNotSupportedException;
import com.project.domain.mapper.EBookMapper;
import com.project.domain.mapper.LocationMapper;
import com.project.domain.mapper.PhysicalCopyMapper;
import com.project.domain.mapper.ReaderMapper;
import com.project.repository.*;
import jakarta.persistence.NoResultException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class ValidationUtils {

    public static int getNewValueOfAvailableCopies(PhysicalCopyRequest request, PhysicalCopyDTO book) {

        int newValueOfBookCopies = request.getNumberOfCopies();
        int numberOfReservedBooks = book.getNumberOfCopies() - book.getNumberOfCopiesAvailable();

        checkIfValueIsNotCorrectlyValidated(newValueOfBookCopies);

        if(newValueOfBookCopies < numberOfReservedBooks){
            throw new ValueNotSupportedException("The requested value: " + newValueOfBookCopies +
                    " can not be applied for the number of copies");
        }

        return newValueOfBookCopies - numberOfReservedBooks;
    }

    public static LocationEntity getTheLocationObject(PhysicalCopyRequest request, LocationRepository locationRepository) {

        LocationEntity location = locationRepository.findByShelfName(request.getLocationName());

        checkIfValueIsNull(location);

        checkIfGenreEquallyValuedToLocationExist(request,location);

        return location;

    }

    public static PhysicalCopyDTO checkIfBookWithGivenTitleAndAuthorAlreadyExists(Integer id,
                                                                                  PhysicalCopyRequest request,
                                                                                  PhysicalCopyRepository repository){
        PhysicalCopyDTO book = PhysicalCopyMapper.toDTO(repository.findByTitleAndAuthor(request.getTitle(),
                request.getAuthor()));

        if(book != null){
            if(!Objects.equals(book.getId(), id)) {
                throw new ResourceAlreadyExistsException("Book with title: " + request.getTitle()  +
                        " and author: " + request.getAuthor() + " already exists with another id: "
                        + book.getId() + " ,therefore you can not commit these changes.");
            }
        }
        return book;
    }

    public static void checkIfBookWithGivenTitleAndAuthorAlreadyExists(PhysicalCopyRequest request,
                                                                       PhysicalCopyRepository repository){
        if((repository.findByTitleAndAuthor(request.getTitle(), request.getAuthor())) != null){
            throw new ResourceAlreadyExistsException("Book with title: " + request.getTitle()  +
                    " and author: " + request.getAuthor() + " already exists.");
        }
    }

    public static boolean isNotReserved(PhysicalCopyDTO book){
        return Objects.equals(book.getNumberOfCopies(), book.getNumberOfCopiesAvailable());
    }

    public static boolean isValueNotNull(PhysicalCopyRequest copyRequest){
        return copyRequest != null;
    }

    public static void checkIfValueIsNotCorrectlyValidated(Integer number){
        if(number <= 0){
            throw new ValueNotSupportedException("The requested value: " + number +
                    " can not be applied for the number of copies");
        }
    }

    public static String checkIfValueIsNotCorrectlyValidated(String value, String field){
        if(value == null){
            throw new NullPointerException("The requested value: " + null +
                    " can not be applied for the requested field: " + field);
        }

        value = value.trim();
        if(value.isEmpty()){
            throw new ValueNotSupportedException("The requested field: " + field +
                    " must contain a value.");
        }

        if (field.equals("password")){
            checkIfPasswordIsNotWithinTheWantedRange(value, field);
        }

        if (value.equalsIgnoreCase("null")) {
            throw new ValueNotSupportedException("The requested value: " + value +
                        " can not be applied for the requested field: " + field);
        }

        if(field.equals("username")){
            checkIfUsernameIsNotWithinTheWantedRange(value, field);
            value = value.replace(" ","");
            value = value.toLowerCase();
        }

        if(field.equals("phoneNumber")) {
            checkIfPhoneNumberIsNotCorrectlyValidated(value);
        }

        if(field.equals("email")){
            value = value.toLowerCase();
            checkIfEmailIsNotCorrectlyValidated(value);
        }

        return value;
    }

    public static void checkIfUsernameIsNotWithinTheWantedRange(String value, String field){
        if(value.length()<3){
            throw new ValueNotSupportedException("The requested field: " + field +
                    " must contain a value with at least 3 letters.");
        }
    }

    public static void checkIfPasswordIsNotWithinTheWantedRange(String value, String field){
        if(value.length()<5){
            throw new ValueNotSupportedException("The requested field: " + field +
                    " must contain a value with at least 5 letters.");
        }
    }

    public static void checkIfPhoneNumberIsNotCorrectlyValidated(String value){
        if(value.length() != 10){
            throw new ValueNotSupportedException("Phone number must contain 10 numbers");
        }else {
            if(!value.startsWith("067")){
                if(!value.startsWith("068")){
                   if(!value.startsWith("069")){
                       throw new ValueNotSupportedException("The value: " + value +
                               " you have provided is not an ALBANIAN number.");
                   }
                }
            }
            int number = Integer.parseInt(value);
            String numberLength = String.valueOf(number);
            int count = numberLength.length() + 1;
            if(count != 10){
                throw new ValueNotSupportedException("The value: " + value +
                        " that you have given does not contain 10 numbers");
            }
        }
    }

    public static void checkIfEmailIsNotCorrectlyValidated(String value){
        if (!isEmailValid(value)){
            throw new ValueNotSupportedException("The given email: " + value +
                    " is not valid");
        }
    }

    public static boolean isEmailValid(String email){
        Matcher matcher = Constants.EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static void checkIfValueIsNotCorrectlyValidated(LocalDateTime dateTime,
                                                           LocalDateTime currentDateTime,
                                                           int requestedDays){
        if(isDateTimeValid(dateTime)){
            checkIfValueIsWithinTheWantedRange(dateTime,currentDateTime,requestedDays);
        } else {
            throw new ValueNotSupportedException("The given date and time: " + dateTime +
                    " is not valid");
        }
    }

    public static void checkIfValueIsWithinTheWantedRange(LocalDateTime dateTime,
                                                          LocalDateTime currentDateTime,
                                                          int requestedDays){
        LocalDateTime maxReservationDateTime = currentDateTime.plusDays(requestedDays);
        if(dateTime.isAfter(maxReservationDateTime) || dateTime.isBefore(currentDateTime)){
            throw new ValueNotSupportedException("The given date and time: " + dateTime +
                    " is not valid. You can not make a reservation till before: " +
                    maxReservationDateTime + " and till after: " + currentDateTime);
        }
    }

    public static boolean isDateTimeValid(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN);
        try {
            String formattedDateTime = dateTime.format(formatter);
            return formattedDateTime.matches(Constants.DATE_TIME_REGEX);
        } catch (DateTimeException e){
            return false;
        }
    }

    public static void checkIfEBookWithGivenTitleAndAuthorAlreadyExists(Integer id, EBookRequest request,
                                                                        EBookRepository repository){
        EBookDTO ebook = EBookMapper.toDTO(repository.findByTitleAndAuthor(request.getTitle(),
                request.getAuthor()));
        if(ebook != null) {
            if (!Objects.equals(ebook.getId(), id)) {
                throw new ResourceAlreadyExistsException("Ebook with title: " + request.getTitle() +
                        " and author: " + request.getAuthor() + " already exists with another id: "
                        + ebook.getId() + " ,therefore you can not commit these changes.");
            }
        }
    }

    public static void checkIfEBookWithGivenTitleAndAuthorAlreadyExists(EBookRequest request,
                                                                        EBookRepository repository){
        if((repository.findByTitleAndAuthor(request.getTitle(), request.getAuthor())) != null){
            throw new ResourceAlreadyExistsException("EBook with title: " + request.getTitle()  +
                    " and author: " + request.getAuthor() + " already exists.");
        }
    }

    public static void checkIfLocationWithGivenShelfNameAlreadyExists(String request,
                                                                      LocationRepository repository){
        if(repository.findByShelfName(request) != null){
            throw new ResourceAlreadyExistsException("Location with shelf name: " + request +
                    " already exists.");
        }
    }

    public static void checkIfLocationWithGivenShelfNameAlreadyExists(Integer id, String request,
                                                                      LocationRepository repository){
        LocationDTO location = LocationMapper.toDTO(repository.findByShelfName(request));
        if(location != null) {
            if (!Objects.equals(location.getId(), id)) {
                throw new ResourceAlreadyExistsException("Location with shelf name: "
                        + request + " already exists with another id: "
                        + location.getId() + " ,therefore you can not commit these changes.");
            }
        }
    }

    public static void checkIfValueIsNull(LocationEntity location){
        try {
            if (location == null) {
                throw new NoResultException("No results were found");
            }
        } catch (NoResultException e){
                throw new ResourceNotFoundException("Location with given nameOfTheShelf can not be found.");
        }
    }

    public static String[] getAllGenresSeparated(PhysicalCopyRequest request){
        String [] genres = request.getGenre().split(",");

        for (int i= 0;i<genres.length;i++){
            genres[i] = genres[i].trim();
        }
        return genres;
    }

    public static boolean isGenreEquallyValuedToLocationNonExistent(String[] genres, LocationEntity location){
        boolean isFound = false;
        for (String genre: genres) {
            if(genre.equalsIgnoreCase(location.getNameOfTheShelf())){
                isFound = true;
                break;
            }
        }
        return !isFound;
    }

    public static void checkIfGenreEquallyValuedToLocationExist(PhysicalCopyRequest request,
                                                                LocationEntity location){
        if(isGenreEquallyValuedToLocationNonExistent(getAllGenresSeparated(request), location)){
            throw new ValueNotSupportedException("Location with name: " + request.getLocationName() +
                    " can not be applied for the location, since it is not included in the genre.");
        }
    }

    public static boolean areBooksFoundInGivenLocation(Integer id, LocationRepository repository){
        return repository.findBooksByLocationId(id) != null;
    }

    public static void checkForOnGoingReservations(PhysicalCopyDTO book,
                                                   PhysicalCopyRequest request){
        if(!isNotReserved(book)){
            throw new MethodCanNotBePerformedException("Book with title:" + book.getTitle() +
                    " and author: " + book.getAuthor() + " can not be changed to title: " +
                    request.getTitle() + " and author: " + request.getAuthor() +
                    " due to on going reservations");
        }
    }

    public static void checkIfReaderWithGivenEmailAlreadyExists(ReaderRequest request,
                                                                ReaderRepository repository) {
        if((repository.findByEmail(request.getEmail())) != null){
            throw new ResourceAlreadyExistsException("Reader with email: " + request.getEmail()  +
                    " already exists.");
        }
    }

    public static void checkIfReaderWithGivenEmailAlreadyExists(Integer id, ReaderRequest request,
                                                                ReaderRepository repository){
        ReaderDTO reader = ReaderMapper.toDTO(repository.findByEmail(request.getEmail()));
        if(reader != null) {
            if (!Objects.equals(reader.getId(), id)) {
                throw new ResourceAlreadyExistsException("Reader with email: "
                        + request.getEmail() + " already exists with another id: "
                        + reader.getId() + " ,therefore you can not commit these changes.");
            }
        }
    }

    public static void checkIfBookReservationsAreNotReturned(List<ReservationEntity> reservations,
                                                             BookReservationRepository bookReservationRepository){
        for (ReservationEntity reservation : reservations) {
            if (bookReservationRepository.findBookReservationsByReservationIdAndStatusNotReturned(reservation.getId()) != null) {
                throw new MethodCanNotBePerformedException("Reader can not be deleted." + " " +
                        "There are still books that are not returned");
            }
        }
    }

    public static void checkIfBookReservationsAreNotReturned(ReservationEntity reservation, BookReservationRepository bookReservationRepository){
        if (bookReservationRepository.findBookReservationsByReservationIdAndStatusNotReturned(reservation.getId()) != null) {
            throw new MethodCanNotBePerformedException("Reservation can not be deleted." + " " +
                        "There are still books that are not returned");
        }
    }

    public static void turnAllReservationsAndBookReservationsDeletedValuesAsRequested(List<ReservationEntity> reservations,
                                                                                      ReservationRepository reservationRepository,
                                                                                      BookReservationRepository bookReservationRepository,
                                                                                      boolean requestedValue){
        for (ReservationEntity reservation : reservations) {
            turnAllReservationsAndBookReservationsDeletedValuesAsRequested(reservation, reservationRepository,
                    bookReservationRepository, requestedValue);
        }
    }

    public static void turnAllReservationsAndBookReservationsDeletedValuesAsRequested(ReservationEntity reservation,
                                                                                      ReservationRepository reservationRepository,
                                                                                      BookReservationRepository bookReservationRepository,
                                                                                      boolean requestedValue){
        turnAllBookReservationsDeletedValuesAsRequested(reservation,bookReservationRepository,requestedValue);
        reservation.setDeleted(requestedValue);
        if(requestedValue){
            reservationRepository.softDelete(reservation);
        }
    }

    public static void turnAllBookReservationsDeletedValuesAsRequested(ReservationEntity reservation,
                                                                       BookReservationRepository bookReservationRepository,
                                                                       boolean deletedValue){
        List<BookReservationEntity> bookReservations = bookReservationRepository.findBookReservationsByReservationId(reservation.getId());
        if (bookReservations != null) {
            for (BookReservationEntity bookReservation: bookReservations) {
                bookReservation.setDeleted(deletedValue);
                if(deletedValue){
                    bookReservationRepository.softDelete(bookReservation);
                }
            }
        }
    }

    public static boolean checkBeforeDeleteToSoftDeleteIfConditionsAreMet(Integer id,
                                                                           ReservationRepository reservationRepository,
                                                                           BookReservationRepository bookReservationRepository){
        List<ReservationEntity> reservations = reservationRepository.findReservationsByReaderId(id);
        if (reservations != null){
            checkIfBookReservationsAreNotReturned(reservations,bookReservationRepository);
            turnAllReservationsAndBookReservationsDeletedValuesAsRequested(reservations,reservationRepository, bookReservationRepository,true);
            return true;
        }
        return false;
    }

    public static boolean checkBeforeDeleteToSoftDeleteIfConditionsAreMet(ReservationEntity reservation,
                                                                          ReservationRepository reservationRepository,
                                                                          BookReservationRepository bookReservationRepository){
        List<BookReservationEntity> bookReservations = bookReservationRepository.findBookReservationsByReservationId(reservation.getId());
        if (bookReservations != null){
            checkIfBookReservationsAreNotReturned(reservation,bookReservationRepository);
            turnAllReservationsAndBookReservationsDeletedValuesAsRequested(reservation,reservationRepository, bookReservationRepository,true);
            return true;
        }
        return false;
    }

    public static void checkIfReservationAlreadyExists(Integer id, ReservationRepository repository){
        List<ReservationEntity> reservations = repository.findReservationsByReaderId(id);
        LocalDate date = LocalDate.now();
        int compared;
        if(reservations != null){
            for (ReservationEntity reservation: reservations) {
                compared = reservation.getCreatedDate().toLocalDate().compareTo(date);
                if(compared == 0){
                    throw new ResourceAlreadyExistsException("Reservation exists with id: " +
                            reservation.getId() + ". You can only create one reservation per day");
                }
            }
        }
    }

    public static boolean areThereAnyBookReservationsForTheGivenBook(Integer id, BookReservationRepository bookReservationRepository){
        List<BookReservationEntity> bookReservations = bookReservationRepository.findBookReservationsByBookId(id);
        return bookReservations != null;
    }

    public static void checkForBookAccessibility(Integer id, PhysicalCopyDTO book){
        if (book.getNumberOfCopies() == 0){
            throw new MethodCanNotBePerformedException("Book with id:" + id +
                    " can not be accessed for reservation anymore, " +
                    " but it's existence is important for the records," +
                    " therefore you can not fully delete it.");
        }
    }

}
