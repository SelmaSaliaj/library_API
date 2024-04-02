package com.project.domain.mapper;

import com.project.domain.dto.PhysicalCopyDTO;
import com.project.domain.dto.PhysicalCopyRequest;
import com.project.domain.entity.LocationEntity;
import com.project.domain.entity.PhysicalCopyEntity;
import com.project.util.ValidationUtils;

public class PhysicalCopyMapper {

    public static PhysicalCopyEntity toEntity(PhysicalCopyRequest copyRequest){
        if(ValidationUtils.isValueNotNull(copyRequest)){
            PhysicalCopyEntity toReturn = new PhysicalCopyEntity();
            toReturn.setTitle(copyRequest.getTitle());
            toReturn.setAuthor(copyRequest.getAuthor());
            toReturn.setGenre(copyRequest.getGenre());
            toReturn.setNumberOfCopiesAvailable(copyRequest.getNumberOfCopies());
            toReturn.setNumberOfCopies(copyRequest.getNumberOfCopies());
            return toReturn;
        }
        return null;
    }

    public static PhysicalCopyEntity toEntity(PhysicalCopyRequest copyRequest , LocationEntity location){
        if(ValidationUtils.isValueNotNull(copyRequest)){
            PhysicalCopyEntity toReturn = new PhysicalCopyEntity();
            toReturn.setTitle(copyRequest.getTitle());
            toReturn.setAuthor(copyRequest.getAuthor());
            toReturn.setGenre(copyRequest.getGenre());
            toReturn.setNumberOfCopiesAvailable(copyRequest.getNumberOfCopies());
            toReturn.setNumberOfCopies(copyRequest.getNumberOfCopies());
            toReturn.setLocation(location);
            return toReturn;
        }
        return null;
    }

    public static PhysicalCopyDTO toDTO(PhysicalCopyEntity physicalCopy){
        if(physicalCopy == null){
            return null;
        }
        PhysicalCopyDTO toReturn = new PhysicalCopyDTO();
        toReturn.setId(physicalCopy.getId());
        toReturn.setTitle(physicalCopy.getTitle());
        toReturn.setAuthor(physicalCopy.getAuthor());
        toReturn.setGenre(physicalCopy.getGenre());
        toReturn.setLocation(LocationMapper.toDTO(physicalCopy.getLocation()));
        toReturn.setNumberOfCopies(physicalCopy.getNumberOfCopies());
        toReturn.setNumberOfCopiesAvailable(physicalCopy.getNumberOfCopiesAvailable());
        return toReturn;
    }

    public static PhysicalCopyEntity toEntity(PhysicalCopyDTO copy){
        if(copy == null){
            return null;
        }
        PhysicalCopyEntity toReturn = new PhysicalCopyEntity();
        toReturn.setId(copy.getId());
        toReturn.setTitle(copy.getTitle());
        toReturn.setAuthor(copy.getAuthor());
        toReturn.setGenre(copy.getGenre());
        toReturn.setLocation(LocationMapper.toEntity(copy.getLocation()));
        toReturn.setNumberOfCopies(copy.getNumberOfCopies());
        toReturn.setNumberOfCopiesAvailable(copy.getNumberOfCopiesAvailable());
        return toReturn;
    }

}
