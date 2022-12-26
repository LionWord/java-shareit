package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {

    Item addItem(int userId, ItemDto itemDto);

    Item editItem(int itemId, ItemDto itemDto);

    Item getItem(int itemId);

    List<Item> getAllMyItems(int userId);

    List<Item> searchItem(String query);

}
