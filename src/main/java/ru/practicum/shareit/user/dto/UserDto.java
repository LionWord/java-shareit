package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class UserDto {
    private String name;
    @Email
    private String email;

}
