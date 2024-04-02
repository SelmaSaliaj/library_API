package com.project.service;

import com.project.domain.dto.LocationDTO;

import java.util.List;

public interface LocationService {

    LocationDTO findById(Integer id);

    LocationDTO save(String request);

    LocationDTO update(Integer id, String request);

    LocationDTO delete(Integer id);

    List<LocationDTO> getAllLocations(int pageNumber, int pageSize);

}
