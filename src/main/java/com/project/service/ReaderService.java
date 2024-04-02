package com.project.service;

import com.project.domain.dto.ReaderDTO;
import com.project.domain.dto.ReaderRequest;

import java.util.List;

public interface ReaderService {

    ReaderDTO findById(Integer id);

    ReaderDTO findByIdAndDeletedValueFalse(Integer id);

    ReaderDTO save(ReaderRequest request);

    ReaderDTO update(Integer id, ReaderRequest request);

    ReaderDTO delete(Integer id);

    List<ReaderDTO> getAllReaders(int pageNumber, int pageSize);

}
