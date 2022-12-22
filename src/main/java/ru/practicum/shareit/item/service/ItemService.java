package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem (int userId, ItemDto itemDto);

    ItemDto editItem(int userId, int itemId);

    ItemDto getItem(int itemId);

    List<ItemDto> getAllMyItems(int userId);

    List<ItemDto> searchItem(String query);

}
