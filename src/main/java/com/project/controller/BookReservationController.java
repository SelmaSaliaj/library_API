package com.project.controller;

import com.project.domain.dto.BookReservationDTO;
import com.project.domain.dto.BookReservationRequest;
import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.ReservationDTO;
import com.project.domain.entity.UserEntity;
import com.project.domain.exception.ResourceNotFoundException;
import com.project.service.BookReservationService;
import com.project.service.ReaderService;
import com.project.service.ReservationService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookReservation")
public class BookReservationController {

    private final BookReservationService bookReservationService;
    private final ReservationService reservationService;
    private final ReaderService readerService;
    private final UserService userService;

    @Autowired
    public BookReservationController(BookReservationService bookReservationService, ReservationService reservationService, ReaderService readerService,
                                     UserService userService) {
        this.bookReservationService = bookReservationService;
        this.reservationService = reservationService;
        this.readerService = readerService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@bookReservationController.isAuthorizedToAccessReservation(authentication,#id)")
    public ResponseEntity<BookReservationDTO> findById(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedToAccessBookReservation(authentication, id)) {
            return ResponseEntity.ok(bookReservationService.findByIdAndDeletedValueFalse(id));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/{readerId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void save(@PathVariable Integer readerId, @RequestBody BookReservationRequest request,
                     Authentication authentication){
        if(isAuthorizedReader(authentication, readerId)) {
            bookReservationService.save(readerId, request);
        } else {
            throw new AccessDeniedException("You can not access book reservations of reader with id: " + readerId);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(@PathVariable Integer id,
                       @RequestParam("newToReturnDateTime")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newToReturnDateTime,
                       Authentication authentication){
        if(isAuthorizedToAccessBookReservation(authentication, id)) {
            bookReservationService.update(id, newToReturnDateTime);
        } else {
            throw new AccessDeniedException("You can not access book reservations of reader with id: " + id);
        }
    }

    @PatchMapping("/statusChange/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateStatusToReturned(@PathVariable Integer id){
        bookReservationService.updateStatusToReturned(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void delete(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedToAccessBookReservation(authentication, id)) {
            bookReservationService.delete(id);
        } else {
            throw new AccessDeniedException("You can not access book reservations of reader with id: " + id);
        }
    }

    @GetMapping("/reservation/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<BookReservationDTO>> findBookReservationsByReservationId(@PathVariable Integer id,
                                                                                        Authentication authentication){
        if(isAuthorizedToAccessReservation(authentication, id)) {
            List<BookReservationDTO> list = bookReservationService.findBookReservationsByReservationId(id);
            if(list == null){
                throw new ResourceNotFoundException("There are no book reservations for reservation with id: "
                        + id);
            }
            return ResponseEntity.ok(list);
        } else {
            throw new AccessDeniedException("You can not access book reservations of reader with id: " + id);
        }
    }

    @GetMapping("/reservation/{id}/notReturned/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<BookReservationDTO>> findBookReservationsByReservationIdAndStatusNotReturned(
            @PathVariable Integer id, Authentication authentication){
        if(isAuthorizedToAccessReservation(authentication, id)) {
            return ResponseEntity.ok(bookReservationService.findBookReservationsByReservationIdAndStatusNotReturned(id));
        } else {
            throw new AccessDeniedException("You can not access book reservations of reader with id: " + id);
        }
    }



    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookReservationDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(bookReservationService.getAllBookReservations(pageNumber,size));
    }

    private boolean isAuthorizedToAccessBookReservation(Authentication authentication, Integer id) {
        String authenticatedUsername = authentication.getName();
        String authority = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if(authority.equals("ROLE_ADMIN")){
            return true;
        }
        UserEntity user = (UserEntity) userService.loadUserByUsername(authenticatedUsername);
        ReaderDTO reader = readerService.findById(user.getReader().getId());
        List<ReservationDTO> reservations = reservationService.findReservationsByReaderId(reader.getId());
        for (ReservationDTO reservation: reservations) {
            List<BookReservationDTO> bookReservations = bookReservationService.findBookReservationsByReservationId(reservation.getId());
            if(bookReservations != null){
                for (BookReservationDTO bookReservation: bookReservations) {
                    if(Objects.equals(bookReservation.getId(), id)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAuthorizedReader(Authentication authentication, Integer readerId) {
        String authenticatedUsername = authentication.getName();
        String authority = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if(authority.equals("ROLE_ADMIN")){
            return true;
        }
        UserEntity user = (UserEntity) userService.loadUserByUsername(authenticatedUsername);
        ReaderDTO readerDTO = readerService.findById(user.getReader().getId());
        return Objects.equals(readerDTO.getId(), readerId);
    }

    public boolean isAuthorizedToAccessReservation(Authentication authentication, Integer id) {
        String authenticatedUsername = authentication.getName();
        String authority = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if(authority.equals("ROLE_ADMIN")){
            return true;
        }
        ReservationDTO reservation = reservationService.findById(id);
        UserEntity user = (UserEntity) userService.loadUserByUsername(authenticatedUsername);
        ReaderDTO reader = readerService.findById(user.getReader().getId());
        return Objects.equals(reservation.getReader().getId(), reader.getId());
    }

}
