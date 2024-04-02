package com.project.domain.dto;

import com.project.domain.enums.BookReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookReservationDTO extends BaseDTO{

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private LocalDateTime returnedDate;

    @NotNull
    private LocalDateTime lastModified;

    private BookReservationStatus status;

    private boolean deleted;

    private int changed;

    private ReservationDTO reservation;

    private PhysicalCopyDTO book;

}
