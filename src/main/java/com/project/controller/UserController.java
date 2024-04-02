package com.project.controller;

import com.project.domain.dto.UserDTO;
import com.project.domain.dto.UserRequest;
import com.project.domain.entity.UserEntity;
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
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedToFindUser(authentication,id)) {
            return ResponseEntity.ok(userService.findById(id));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public void save(@RequestBody @Valid UserRequest user){
        userService.save(user);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public void update(@PathVariable Integer id, @RequestParam String username, @RequestParam String password,
                       Authentication authentication){
        if(isAuthorizedUser(authentication,id)) {
            userService.update(id, username, password);
        } else {
            throw new AccessDeniedException("You can not access user with id: " + id);
        }
    }

    @PatchMapping("/changeStatus/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateAuthority(@PathVariable Integer id){
        userService.updateAuthority(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public void delete(@PathVariable("id") Integer id, Authentication authentication){
        if(isAuthorizedUser(authentication,id)){
            userService.delete(id);
        }else{
            throw new AccessDeniedException("You can not access user with id: " + id);
        }
    }

    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(userService.getAllUsers(pageNumber,size));
    }

    private boolean isAuthorizedUser(Authentication authentication, Integer id) {
        String authenticatedUsername = authentication.getName();
        UserEntity user = (UserEntity) userService.loadUserByUsername(authenticatedUsername);
        return Objects.equals(user.getId(), id);
    }

    private boolean isAuthorizedToFindUser(Authentication authentication, Integer id) {
        String authenticatedUsername = authentication.getName();
        String authority = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        if(authority.equals("ROLE_ADMIN")){
            return true;
        }
        UserEntity user = (UserEntity) userService.loadUserByUsername(authenticatedUsername);
        return Objects.equals(user.getId(), id);
    }

}
