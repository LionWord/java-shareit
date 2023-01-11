package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.comments.CommentDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ItemCommentsDto extends ItemForOwnerDto {
    private List<CommentDto> comments = new ArrayList<>();
}
