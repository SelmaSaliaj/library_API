package com.project.controller;

import com.project.domain.dto.EBookDTO;
import com.project.domain.dto.EBookRequest;
import com.project.service.EBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EBookController {

    private final EBookService eBookService;

    @Autowired
    public EBookController(EBookService eBookService) {
        this.eBookService = eBookService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EBookDTO> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(eBookService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void save(@RequestBody @Valid EBookRequest ebook){
        eBookService.save(ebook);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(@PathVariable Integer id,@RequestBody @Valid EBookRequest ebook){
        eBookService.update(id, ebook);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("id") Integer id){
        eBookService.delete(id);
    }

    @GetMapping("/byAuthor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<EBookDTO>> findByAuthor(@RequestParam String author){
        List<EBookDTO> list = eBookService.findByAuthor(author);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/byTitle")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<EBookDTO>> findByTitle(@RequestParam String title){
        List<EBookDTO> list = eBookService.findByTitle(title);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/byTitleAndAuthor")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<EBookDTO> findByTitleAndAuthor(@RequestParam String title, @RequestParam String author){
        EBookDTO ebook = eBookService.findByTitleAndAuthor(title,author);
        return ResponseEntity.ok(ebook);
    }

    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EBookDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(eBookService.getAllEBooks(pageNumber,size));
    }

}
