package com.project.service.impl;

import com.project.domain.dto.UserDTO;
import com.project.domain.dto.UserRequest;
import com.project.domain.entity.ReaderEntity;
import com.project.domain.entity.UserEntity;
import com.project.domain.exception.MethodCanNotBePerformedException;
import com.project.domain.exception.ResourceAlreadyExistsException;
import com.project.domain.mapper.ReaderMapper;
import com.project.domain.mapper.UserMapper;
import com.project.repository.UserRepository;
import com.project.service.ReaderService;
import com.project.service.UserService;
import com.project.util.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final ReaderService readerService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, ReaderService readerService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.readerService = readerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO findById(Integer id) {
        return UserMapper.toDTO(repository.findById(id));
    }

    @Transactional
    @Override
    public void save(UserRequest request) {
        request.setUsername(ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getUsername(),"username"));
        String password = ValidationUtils.checkIfValueIsNotCorrectlyValidated(request.getPassword(),"password");

        if(repository.findByUsername(request.getUsername()) != null){
            throw new ResourceAlreadyExistsException("User with username: " + request.getUsername() +
                    " already exists.");
        }

        request.setPassword(passwordEncoder.encode(password));

        ReaderEntity reader = ReaderMapper.toEntity(readerService.save(request.getReader()));

        UserEntity user = UserMapper.toEntity(request);
        user.setReader(reader);
        user.setAuthorities("USER");

        UserMapper.toDTO(repository.save(user));
    }

    @Override
    public void update(Integer id, String username, String password) {
        UserEntity actualUser = repository.findById(id);

        username = ValidationUtils.checkIfValueIsNotCorrectlyValidated(username,"username");
        password = ValidationUtils.checkIfValueIsNotCorrectlyValidated(password,"password");

        UserEntity user = repository.findByUsername(username);
        if(user != null){
            if (!Objects.equals(user.getId(), id)){
                throw new ResourceAlreadyExistsException("User with username: " + username +
                        " already exists with another id: " + user.getId());
            }
        }
        password = passwordEncoder.encode(password);
        actualUser.setUsername(username);
        actualUser.setPassword(password);
        UserMapper.toDTO(repository.update(actualUser));
    }

    @Override
    public void updateAuthority(Integer id) {
        UserEntity user = repository.findById(id);
        if(Objects.equals(user.authoritiesToString(),"ROLE_ADMIN")){
            throw new MethodCanNotBePerformedException("You are already authorized as: " + user.authoritiesToString());
        }
        user.setAuthorities("ADMIN");
        ReaderEntity reader = user.getReader();
        user.setReader(null);
        readerService.delete(reader.getId());
        UserMapper.toDTO(repository.update(user));
    }


    @Override
    public void delete(Integer id) {
        UserEntity user = repository.findById(id);
        if(Objects.equals(user.authoritiesToString(), "ROLE_USER")) {
            ReaderEntity reader = user.getReader();
            if(reader != null) {
                readerService.delete(reader.getId());
                user.setReader(null);
            }
        }
        user = repository.update(user);
        repository.delete(user);
        UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers(int pageNumber, int pageSize) {
        List<UserEntity> list = repository.getAll(pageNumber,pageSize);
        return list.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("This user is not valid.");
        }
        return user;
    }
}
