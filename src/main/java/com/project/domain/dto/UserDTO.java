package com.project.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseDTO{

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String authorities;

    private ReaderDTO reader;

    public UserDTO(Integer id, String username, String authorities, ReaderDTO reader) {
        super(id);
        this.username = username;
        this.authorities = authorities;
        this.reader = reader;
    }

}
