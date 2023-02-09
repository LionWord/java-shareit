package ru.practicum.shareit.request.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class Request {
    @NotBlank
    @Length(max = 256)
    private String description;

}
