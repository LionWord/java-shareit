package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") int userId,
                        @RequestBody ItemDto item) {
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@RequestHeader("X-Sharer-User-Id") int userId,
                         @PathVariable int itemId,
                         @RequestBody ItemDto item) {
        return itemService.editItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDatesCommentsDto getItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.addDatesAndComments(userId, itemService.getItem(itemId));
    }

    @GetMapping
    public List<Item> getAllMyItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @RequestParam(name = "from", required = false) Integer from,
                                    @RequestParam(name = "size", required = false) Integer size) {
        return itemService.getAllMyItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam(name = "text") String query,
                                 @RequestParam(name = "from", required = false) Integer from,
                                 @RequestParam(name = "size", required = false) Integer size) {
        return itemService.searchItem(query, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                  @PathVariable int itemId,
                                  @RequestBody Comment comment) {
        return itemService.postComment(userId, itemId, comment);
    }

}
