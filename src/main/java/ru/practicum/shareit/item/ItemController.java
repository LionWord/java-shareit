package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.InvalidItemInputException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.ItemValidator;
import ru.practicum.shareit.utils.Messages;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody ItemDto itemDto) {
        if (!ItemValidator.isValidItem(itemDto) | !itemDto.isAvailable()) {
            throw new InvalidItemInputException(Messages.INVALID_ITEM_INPUT);
        } else if (!userService.idIsPresent(userId)) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        return itemService.addItem(userId, itemDto);
    }
    @PatchMapping("/{itemId}")
    public Item editItem(@RequestHeader("X-Sharer-User-Id") int userId,
                         @PathVariable int itemId,
                         @RequestBody ItemDto item) {
        if (!userService.idIsPresent(userId)) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        return itemService.editItem(itemId, item);
    }
    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }
    @GetMapping
    public List<Item> getAllMyItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAllMyItems(userId);
    }
    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam(name = "text") String query) {
        return itemService.searchItem(query);
    }

}
