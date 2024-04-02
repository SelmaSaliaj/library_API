package com.project.repository;

import com.project.domain.entity.LocationEntity;
import com.project.domain.entity.PhysicalCopyEntity;

import java.util.List;

public interface LocationRepository extends BaseRepository<LocationEntity,Integer>{

    LocationEntity findByShelfName(String nameOfTheShelf);

    List<PhysicalCopyEntity> findBooksByLocationId(Integer id);

}
