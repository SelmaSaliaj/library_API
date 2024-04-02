package com.project.repository;

import com.project.domain.entity.PhysicalCopyEntity;

import java.util.List;

public interface PhysicalCopyRepository extends BaseRepository<PhysicalCopyEntity,Integer> {

    List<PhysicalCopyEntity> findByTitle(String title);

    List<PhysicalCopyEntity> findByAuthor(String author);

    PhysicalCopyEntity findByTitleAndAuthor(String title, String author);

}
