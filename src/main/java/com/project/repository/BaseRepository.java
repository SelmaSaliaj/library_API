package com.project.repository;

import com.project.domain.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository <T extends BaseEntity, I extends Serializable>{

    T save(T entity);

    T update(T entity);

    T findById(I id);

    T delete(T entity);

    List<T> getAll(int pageNumber, int pageSize);

}
