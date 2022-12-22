package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User_Id") int userId, ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }
    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User_Id") int userId, int itemId) {
        return itemService.editItem(userId, itemId);
    }
    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }
    @GetMapping
    public List<ItemDto> getAllMyItems(@RequestHeader("X-Sharer-User_Id") int userId) {
        return itemService.getAllMyItems(userId);
    }
    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(name = "text") String query) {
        return itemService.searchItem(query);
    }

}
