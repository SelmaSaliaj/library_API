package com.project.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO extends BaseDTO{

    @NotBlank(message = "The name of the shelf is required")
    private String nameOfTheShelf;

}
