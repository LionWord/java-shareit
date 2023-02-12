package ru.practicum.shareit.item.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class ItemDto {

    @NotBlank
    @Length(max = 64)
    private String name;

    @NotBlank
    @Length(max = 256)
    private String description;

    @NotNull
    private Boolean available;

    @Min(1)
    private Integer requestId;
}
