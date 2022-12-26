package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.IdAssigner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImplInMemory implements ItemDao {

    private final Map<Integer, Item> items = new HashMap<>();

    public Item addItem(int userId, ItemDto itemDto) {
        Item item = ItemMapper.makeItemFromDto(userId, itemDto);
        IdAssigner.assignItemId(item);
        items.put(item.getId(), item);
        return item;
    }

    public Item editItem(int itemId, ItemDto itemDto) {
        Item item = items.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        items.put(itemId, item);
        return getItem(itemId);
    }

    public Item getItem(int itemId) {
        return items.get(itemId);
    }

    public List<Item> getAllMyItems(int userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    public List<Item> searchItem(String request) {
        if (request.isEmpty()) {
            return List.of();
        }
        String query = request.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(query)
                        | item.getDescription().toLowerCase().contains(query))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());

    }

}
