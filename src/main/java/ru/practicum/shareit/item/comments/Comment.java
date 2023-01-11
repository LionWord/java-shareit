package ru.practicum.shareit.item.comments;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "comments")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;
    @Column(name = "comment_text")
    private String text;
    @Column(name = "item_id")
    private int itemId;
    @Column(name = "author_id")
    private int authorId;
}
