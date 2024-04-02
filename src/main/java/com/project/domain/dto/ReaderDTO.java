package com.project.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderDTO extends BaseDTO{

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull
    private String address;

    private String phoneNumber;

    private boolean deleted;

    public ReaderDTO(Integer id, String name, String surname, String email, String address, String phoneNumber) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

}
