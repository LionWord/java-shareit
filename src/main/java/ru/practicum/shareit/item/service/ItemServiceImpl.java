package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item saveItem(int userId, Item item) {
        item.setOwnerId(userId);
        return itemRepository.save(item);
    }

    @Override
    public Item editItem(int itemId, ItemDto item) {
        Item myItem = getItem(itemId).get();
        itemRepository.updateItem(item, myItem);
        return saveItem(myItem.getOwnerId(), myItem);
    }
    @Override
    public Optional<Item> getItem(int itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> getAllMyItems(int userId) {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> searchItem(String query) {
        return itemRepository.findAllByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(query, query);
    }

}
