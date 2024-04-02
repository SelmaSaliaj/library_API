package com.project.controller;

import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.ReaderRequest;
import com.project.domain.entity.UserEntity;
import com.project.service.ReaderService;
import com.project.service.UserService;
import jakarta.validation.Valid;
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
@RequestMapping("/reader")
public class ReaderController {

    private final ReaderService readerService;
    private final UserService userService;

    @Autowired
    public ReaderController(ReaderService readerService, UserService userService) {
        this.readerService = readerService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ReaderDTO> findById(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedReader(authentication, id)){
            return ResponseEntity.ok(readerService.findById(id));
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void update(@PathVariable("id") Integer id, @RequestBody @Valid ReaderRequest reader, Authentication authentication){
        if(isAuthorizedReader(authentication, id)) {
            readerService.update(id, reader);
        } else {
            throw new AccessDeniedException("You can not access reader with id: " + id);
        }
    }

    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ReaderDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(readerService.getAllReaders(pageNumber,size));
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
