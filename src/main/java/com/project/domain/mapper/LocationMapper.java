package com.project.domain.mapper;

import com.project.domain.dto.LocationDTO;
import com.project.domain.entity.LocationEntity;

public class LocationMapper {

    public static LocationEntity toEntity(String locationRequest){
        LocationEntity toReturn = new LocationEntity();
        toReturn.setNameOfTheShelf(locationRequest);
        return toReturn;
    }

    public static LocationDTO toDTO(LocationEntity location){
        if(location == null){
            return null;
        }
        LocationDTO toReturn = new LocationDTO();
        toReturn.setId(location.getId());
        toReturn.setNameOfTheShelf(location.getNameOfTheShelf());
        return toReturn;
    }

    public static LocationEntity toEntity(LocationDTO location){
        if(location == null){
            return null;
        }
        LocationEntity toReturn = new LocationEntity();
        toReturn.setId(location.getId());
        toReturn.setNameOfTheShelf(location.getNameOfTheShelf());
        return toReturn;
    }

}
