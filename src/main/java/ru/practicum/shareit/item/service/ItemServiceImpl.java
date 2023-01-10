package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.utils.Messages;

import javax.persistence.EntityManagerFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item saveItem(int userId, ItemDto item) {
        Item itemEntity = itemRepository.itemFromDto(item);
        itemEntity.setOwnerId(userId);
        return itemRepository.save(itemEntity);
    }

    @Override
    public Item editItem(int itemId, ItemDto item) {
        Item myItem = getItem(itemId).get();
        itemRepository.updateItem(item, myItem);
        return itemRepository.save(myItem);
    }
    @Override
    public Optional<Item> getItem(int itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NoSuchItemException(Messages.NO_SUCH_ITEM);
        }
        return item;
    }

    @Override
    public List<Item> getAllMyItems(int userId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwnerId() == userId)
                .sorted(Comparator.comparingInt(Item::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String query) {
        if (query.isEmpty()) {
            return List.of();
        }
        return itemRepository.findAllByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(query, query).stream()
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

}
