package com.project.service;

import com.project.domain.dto.PhysicalCopyDTO;
import com.project.domain.dto.PhysicalCopyRequest;

import java.util.List;

public interface PhysicalCopyService{

    PhysicalCopyDTO findById(Integer id);

    PhysicalCopyDTO save(PhysicalCopyRequest request);

    PhysicalCopyDTO update(Integer id, PhysicalCopyRequest request);

    PhysicalCopyDTO update(PhysicalCopyDTO book);

    PhysicalCopyDTO delete(Integer id);

    List<PhysicalCopyDTO> findByTitle(String title);

    List<PhysicalCopyDTO> findByAuthor(String author);

    PhysicalCopyDTO findByTitleAndAuthor(String title, String author);

    List<PhysicalCopyDTO> getAllPhysicalBooks(int pageNumber, int pageSize);

}
