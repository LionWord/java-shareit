package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item saveItem(int userId, ItemDto item);

    Item editItem(int userId, int itemId, ItemDto item);

    Item getItem(int itemId);

    ItemDatesCommentsDto addDatesAndComments(int userId, Item item);

    List<Item> getAllMyItems(int userId);
    List<Item> getAllMyItems(int userId, Integer from, Integer size);

    List<Item> searchItem(String query);
    List<Item> searchItem(String query, Integer from, Integer size);

    CommentDto postComment(int userId, int itemId, Comment comment);

}
