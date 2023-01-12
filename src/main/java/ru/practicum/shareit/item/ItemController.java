package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.CantCommentException;
import ru.practicum.shareit.exceptions.EmptyCommentException;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDatesCommentsMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Messages;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody ItemDto item) {
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@RequestHeader("X-Sharer-User-Id") int userId,
                         @PathVariable int itemId,
                         @RequestBody ItemDto item) {
        return itemService.editItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.addDatesAndComments(userId, itemService.getItem(itemId));
    }

    @GetMapping
    public List<Item> getAllMyItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getAllMyItems(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam(name = "text") String query) {
        return itemService.searchItem(query);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                  @PathVariable int itemId,
                                  @RequestBody Comment comment) {
        return itemService.postComment(userId, itemId, comment);
    }

}
