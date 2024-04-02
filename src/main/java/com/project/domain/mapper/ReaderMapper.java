package com.project.domain.mapper;

import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.ReaderRequest;
import com.project.domain.entity.ReaderEntity;
import com.project.domain.entity.UserEntity;

public class ReaderMapper {

    public static ReaderEntity toEntity(ReaderRequest readerRequest){
        ReaderEntity toReturn = new ReaderEntity();
        toReturn.setName(readerRequest.getName());
        toReturn.setSurname(readerRequest.getSurname());
        toReturn.setAddress(readerRequest.getAddress());
        toReturn.setEmail(readerRequest.getEmail());
        toReturn.setPhoneNumber(readerRequest.getPhoneNumber());
        return toReturn;
    }

    public static ReaderEntity toEntity(ReaderRequest readerRequest, UserEntity user){
        ReaderEntity toReturn = new ReaderEntity();
        toReturn.setName(readerRequest.getName());
        toReturn.setSurname(readerRequest.getSurname());
        toReturn.setAddress(readerRequest.getAddress());
        toReturn.setEmail(readerRequest.getEmail());
        toReturn.setPhoneNumber(readerRequest.getPhoneNumber());
        toReturn.setUser(user);
        return toReturn;
    }

    public static ReaderDTO toDTO(ReaderEntity reader){
        if(reader == null){
            return null;
        }
        ReaderDTO toReturn = new ReaderDTO();
        toReturn.setId(reader.getId());
        toReturn.setName(reader.getName());
        toReturn.setSurname(reader.getSurname());
        toReturn.setAddress(reader.getAddress());
        toReturn.setEmail(reader.getEmail());
        toReturn.setPhoneNumber(reader.getPhoneNumber());
        toReturn.setDeleted(reader.isDeleted());
        return toReturn;
    }

    public static ReaderEntity toEntity(ReaderDTO reader){
        if(reader == null){
            return null;
        }
        ReaderEntity toReturn = new ReaderEntity();
        toReturn.setId(reader.getId());
        toReturn.setName(reader.getName());
        toReturn.setSurname(reader.getSurname());
        toReturn.setAddress(reader.getAddress());
        toReturn.setEmail(reader.getEmail());
        toReturn.setPhoneNumber(reader.getPhoneNumber());
        return toReturn;
    }

}
