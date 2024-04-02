package com.project.domain.entity;

import com.project.domain.enums.BookReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_reservation")
public class BookReservationEntity extends BaseEntity{

    @CreatedDate
    @Column(updatable = false, name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastModified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "return_date")
    private LocalDateTime returnedDate;

    private boolean deleted;

    private int changed;

    @Enumerated(EnumType.STRING)
    private BookReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "reservation_id" , referencedColumnName = "id")
    private ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "book_id" , referencedColumnName = "isbn")
    private PhysicalCopyEntity book;

}
