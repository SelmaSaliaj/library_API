package com.project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reader")
public class ReaderEntity extends BaseEntity{

    @Column(name = "first_name")
    private String name;

    private String surname;

    @Column(unique = true)
    private String email;

    private String address;

    private String phoneNumber;

    private boolean deleted;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;

    @OneToOne(mappedBy = "reader", cascade = CascadeType.PERSIST)
    private UserEntity user;
}
