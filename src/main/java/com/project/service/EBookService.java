package com.project.service;

import com.project.domain.dto.EBookDTO;
import com.project.domain.dto.EBookRequest;

import java.util.List;

public interface EBookService {

    EBookDTO findById(Integer id);

    EBookDTO save(EBookRequest request);

    EBookDTO update(Integer id, EBookRequest request);

    EBookDTO delete(Integer id);

    List<EBookDTO> findByTitle(String title);

    List<EBookDTO> findByAuthor(String author);

    EBookDTO findByTitleAndAuthor(String title, String author);

    List<EBookDTO> getAllEBooks(int pageNumber, int pageSize);

}
