package com.project.domain.mapper;

import com.project.domain.dto.UserDTO;
import com.project.domain.dto.UserRequest;
import com.project.domain.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(UserRequest userRequest){
        if(userRequest == null){
            return null;
        }
        UserEntity toReturn = new UserEntity();
        toReturn.setUsername(userRequest.getUsername());
        toReturn.setPassword(userRequest.getPassword());
        return toReturn;
    }

    public static UserDTO toDTO(UserEntity user){
        if(user == null){
            return null;
        }
        UserDTO toReturn = new UserDTO();
        toReturn.setId(user.getId());
        toReturn.setUsername(user.getUsername());
        toReturn.setAuthorities(user.getAuthorities().toString());
        toReturn.setReader(ReaderMapper.toDTO(user.getReader()));
        return toReturn;
    }

    public static UserEntity toEntity(UserDTO user){
        if(user == null){
            return null;
        }
        UserEntity toReturn = new UserEntity();
        toReturn.setId(user.getId());
        toReturn.setUsername(user.getUsername());
        toReturn.setAuthorities(user.getAuthorities());
        toReturn.setReader(ReaderMapper.toEntity(user.getReader()));
        return toReturn;
    }

    public static UserDTO toDTO(UserRequest userRequest){
        if(userRequest == null){
            return null;
        }
        UserDTO toReturn = new UserDTO();
        toReturn.setUsername(userRequest.getUsername());
        return toReturn;
    }

}
