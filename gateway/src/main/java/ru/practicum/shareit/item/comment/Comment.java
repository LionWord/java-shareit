package ru.practicum.shareit.item.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Comment {
    @NotBlank
    private String text;

}
