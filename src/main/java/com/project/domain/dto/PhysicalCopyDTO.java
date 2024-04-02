package com.project.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalCopyDTO extends BaseDTO{

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotNull
    private String genre;

    @Min(value = 1, message = "Value should not be below 1")
    private Integer numberOfCopies;

    @PositiveOrZero(message = "Values below 0 are NOT allowed")
    private Integer numberOfCopiesAvailable;

    @NotNull
    private LocationDTO location;

}
