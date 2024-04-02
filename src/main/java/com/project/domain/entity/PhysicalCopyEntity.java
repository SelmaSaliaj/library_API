package com.project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
@AttributeOverride(name = "id", column = @Column(name = "isbn"))
public class PhysicalCopyEntity extends BookEntity{

    @Column(name = "copies")
    private Integer numberOfCopies;

    @Column(name = "copies_available")
    private Integer numberOfCopiesAvailable;

    @OneToOne
    @JoinColumn(name = "location_id" , referencedColumnName = "id")
    private LocationEntity location;

    @OneToMany(mappedBy = "book")
    private List<BookReservationEntity> bookReservation = new ArrayList<>();

}
