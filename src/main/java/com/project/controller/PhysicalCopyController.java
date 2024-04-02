package com.project.controller;

import com.project.domain.dto.PhysicalCopyDTO;
import com.project.domain.dto.PhysicalCopyRequest;
import com.project.service.PhysicalCopyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class PhysicalCopyController {

    private final PhysicalCopyService bookService;

    @Autowired
    public PhysicalCopyController(PhysicalCopyService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<PhysicalCopyDTO> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/byAuthor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<PhysicalCopyDTO>> findByAuthor(@RequestParam String author){
        return ResponseEntity.ok(bookService.findByAuthor(author));
    }

    @GetMapping("/byTitle")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<PhysicalCopyDTO>> findByTitle(@RequestParam String title){
        return ResponseEntity.ok(bookService.findByTitle(title));
    }

    @GetMapping("/byTitleAndAuthor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<PhysicalCopyDTO> findByTitleAndAuthor(@RequestParam String title, @RequestParam String author){
        return ResponseEntity.ok(bookService.findByTitleAndAuthor(title,author));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void save(@RequestBody @Valid PhysicalCopyRequest physicalCopy){
        bookService.save(physicalCopy);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(@PathVariable Integer id, @RequestBody @Valid PhysicalCopyRequest physicalCopy){
        bookService.update(id,physicalCopy);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("id") Integer id){
        bookService.delete(id);
    }

    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PhysicalCopyDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(bookService.getAllPhysicalBooks(pageNumber,size));
    }

}
