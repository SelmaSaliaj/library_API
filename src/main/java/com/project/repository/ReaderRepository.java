package com.project.repository;

import com.project.domain.entity.ReaderEntity;

public interface ReaderRepository extends BaseRepository<ReaderEntity,Integer> {

    ReaderEntity findByIdAndDeletedValueFalse(Integer id);

    ReaderEntity softDelete(ReaderEntity entity);

    ReaderEntity findByEmail(String email);

}
