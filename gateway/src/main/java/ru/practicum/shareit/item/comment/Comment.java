package ru.practicum.shareit.item.comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Comment {
    @NotBlank
    @Length(max = 256)
    private String text;

}
