package com.project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "location")
public class LocationEntity extends BaseEntity{

    @Column(name = "shelf_name" , unique = true)
    private String nameOfTheShelf;

}
