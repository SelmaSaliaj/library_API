package com.project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity{

    @CreatedDate
    @Column(updatable = false, name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastModified;

    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "id_reader" , referencedColumnName = "id")
    private ReaderEntity reader;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<BookReservationEntity> bookReservations = new ArrayList<>();

}
