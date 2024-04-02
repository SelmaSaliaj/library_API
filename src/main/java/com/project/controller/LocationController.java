package com.project.controller;

import com.project.domain.dto.LocationDTO;
import com.project.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<LocationDTO> findById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(locationService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void save(@RequestParam String nameOfTheShelf){
        locationService.save(nameOfTheShelf);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void update(@PathVariable Integer id, @RequestParam String nameOfTheShelf){
        locationService.update(id, nameOfTheShelf);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable("id") Integer id){
        locationService.delete(id);
    }

    @GetMapping("/{pageNumber}/{size}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<List<LocationDTO>> getAll(@PathVariable int pageNumber, @PathVariable int size){
        return ResponseEntity.ok(locationService.getAllLocations(pageNumber,size));
    }

}
