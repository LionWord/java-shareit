package ru.practicum.shareit.item.comments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.service.UserService;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto extends Comment {

    private String authorName;

    public static CommentDto MapToDto(Comment comment, UserService userService) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setId(comment.getId());
        commentDto.setItemId(comment.getItemId());
        commentDto.setAuthorId(comment.getAuthorId());
        commentDto.setAuthorName(userService.getUser(comment.getAuthorId()).get().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }
}
