package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class UserDto {
    @NotEmpty
    @NotBlank
    private String name;
    @Email
    private String email;

}
