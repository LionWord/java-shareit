package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item saveItem(int userId, ItemDto item);

    Item editItem(int itemId, ItemDto item);

    Optional<Item> getItem(int itemId);

    Item addDatesAndComments(int userId, Item item);

    List<Item> getAllMyItems(int userId);

    List<Item> searchItem(String query);
    CommentDto postComment(int userId, int itemId, Comment comment);

}
