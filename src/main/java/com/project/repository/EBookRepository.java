package com.project.repository;

import com.project.domain.entity.EBookEntity;

import java.util.List;

public interface EBookRepository extends BaseRepository<EBookEntity,Integer> {

    List<EBookEntity> findByTitle(String title);

    List<EBookEntity> findByAuthor(String author);

    EBookEntity findByTitleAndAuthor(String title, String author);

}
