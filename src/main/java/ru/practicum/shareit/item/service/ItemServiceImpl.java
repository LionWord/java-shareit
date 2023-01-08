package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;

    public Item addItem(int userId, ItemDto itemDto) {
        return itemDao.addItem(userId, itemDto);
    }

    public Item editItem(int itemId, ItemDto itemDto) {
        return itemDao.editItem(itemId, itemDto);
    }

    public Item getItem(int itemId) {
        return itemDao.getItem(itemId);
    }

    public List<Item> getAllMyItems(int userId) {
        return itemDao.getAllMyItems(userId);
    }

    public List<Item> searchItem(String query) {
        return itemDao.searchItem(query);
    }

}
