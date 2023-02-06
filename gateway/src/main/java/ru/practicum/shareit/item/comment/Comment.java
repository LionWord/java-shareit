package ru.practicum.shareit.item.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {

    private int id;
    @NotBlank
    private String text;
    @NotNull @Min(1)
    private int itemId;
    @NotNull @Min(1)
    private int authorId;

    private LocalDateTime created;

}
