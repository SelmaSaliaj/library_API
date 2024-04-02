package com.project.controller;

import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.ReservationDTO;
import com.project.domain.entity.UserEntity;
import com.project.service.ReaderService;
import com.project.service.ReservationService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReaderService readerService;
    private final UserService userService;

    @Autowired
    public ReservationController(ReservationService reservationService, ReaderService readerService,
                                 UserService userService) {
        this.reservationService = reservationService;
        this.readerService = readerService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ReservationDTO> findById(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedToAccessReservation(authentication,id)) {
            return ResponseEntity.ok(reservationService.findById(id));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/save/{readerId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void save(@PathVariable Integer readerId, Authentication authentication){
        if(isAuthorizedReader(authentication, readerId)) {
        reservationService.save(readerId);
        } else {
            throw new AccessDeniedException("You can not access reservations of reader with id: " + readerId);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void delete(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedToAccessReservation(authentication,id)) {
            reservationService.delete(id);
        } else {
            throw new AccessDeniedException("You can not access reservation with id: " + id);
        }
    }

    @GetMapping("/reader/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<ReservationDTO>> findReservationsByReaderId(@PathVariable Integer id,
                                                                           Authentication authentication){
        if(isAuthorizedReader(authentication, id)) {
            return ResponseEntity.ok(reservationService.findReservationsByReaderId(id));
        } else {
            throw new AccessDeniedException("You can not access reservations of reader with id: " + id);
        }
    }

    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(reservationService.getAllReservations(pageNumber,size));
    }

    private boolean isAuthorizedToAccessReservation(Authentication authentication, Integer id) {
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

    private boolean isAuthorizedReader(Authentication authentication, Integer readerId) {
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



}
