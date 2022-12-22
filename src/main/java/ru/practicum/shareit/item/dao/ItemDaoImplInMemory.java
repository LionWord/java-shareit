package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.IdAssigner;

import java.util.List;
import java.util.Map;

@Repository
public class ItemDaoImplInMemory implements ItemDao {

    private final Map<Integer, Item> items = Map.of();

    public ItemDto addItem (int userId, ItemDto itemDto) {
        Item item = ItemMapper.makeItemFromDto(userId, itemDto);
        IdAssigner.assignItemId(item);
        items.put(item.getId(), item);
        return itemDto;
    }

    public ItemDto editItem(int userId, int itemId) {
        items.put(itemId, items.get(itemId));
        return getItem(itemId);
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.makeDtoFromItem(items.get(itemId));
    }

    public List<ItemDto> getAllMyItems(int userId) {
        List<ItemDto> result = List.of();
        items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .forEach(item -> result.add(ItemMapper.makeDtoFromItem(item)));
        return result;
    }

    public List<ItemDto> searchItem(String query) {
        List<ItemDto> result = List.of();
        items.values().stream()
                .filter(item -> item.getName().contains(query) | item.getDescription().contains(query))
                .filter(item -> item.isAvailable())
                .forEach(item -> result.add(ItemMapper.makeDtoFromItem(item)));
        return result;
    }

}
