package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    public ItemDto addItem (int userId, ItemDto itemDto) {
        return itemDao.addItem(userId, itemDto);
    }

    public ItemDto editItem(int userId, int itemId) {
        return itemDao.editItem(userId, itemId);
    }

    public ItemDto getItem(int itemId) {
        return itemDao.getItem(itemId);
    }

    public List<ItemDto> getAllMyItems(int userId) {
        return itemDao.getAllMyItems(userId);
    }

    public List<ItemDto> searchItem(String query) {
        return itemDao.searchItem(query);
    }

}
