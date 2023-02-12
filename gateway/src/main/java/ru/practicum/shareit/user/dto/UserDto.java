package ru.practicum.shareit.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    @NotEmpty
    @NotBlank
    @Length(max = 64)
    private String name;
    @Email
    @Length(max = 64)
    @NotBlank
    private String email;

}
