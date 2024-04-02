package com.project.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO extends BaseDTO{

    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private LocalDateTime lastModified;

    @NotNull
    private boolean deleted;

    @NotNull
    private ReaderDTO reader;

}
