package com.project.domain.mapper;

import com.project.domain.dto.EBookDTO;
import com.project.domain.dto.EBookRequest;
import com.project.domain.entity.EBookEntity;

public class EBookMapper {

    public static EBookEntity toEntity(EBookRequest eBookRequest){
        EBookEntity toReturn = new EBookEntity();
        toReturn.setTitle(eBookRequest.getTitle());
        toReturn.setAuthor(eBookRequest.getAuthor());
        toReturn.setGenre(eBookRequest.getGenre());
        toReturn.setLink(eBookRequest.getLink());
        return toReturn;
    }

    public static EBookDTO toDTO(EBookEntity eBook){
        if(eBook == null){
            return null;
        }
        EBookDTO toReturn = new EBookDTO();
        toReturn.setId(eBook.getId());
        toReturn.setTitle(eBook.getTitle());
        toReturn.setAuthor(eBook.getAuthor());
        toReturn.setGenre(eBook.getGenre());
        toReturn.setLink(eBook.getLink());
        return toReturn;
    }

    public static EBookEntity toEntity(EBookDTO eBook){
        if(eBook == null){
            return null;
        }
        EBookEntity toReturn = new EBookEntity();
        toReturn.setId(eBook.getId());
        toReturn.setTitle(eBook.getTitle());
        toReturn.setAuthor(eBook.getAuthor());
        toReturn.setGenre(eBook.getGenre());
        toReturn.setLink(eBook.getLink());
        return toReturn;
    }

}
