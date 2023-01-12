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
        if (userService.getUser(userId).isEmpty()) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@RequestHeader("X-Sharer-User-Id") int userId,
                         @PathVariable int itemId,
                         @RequestBody ItemDto item) {
        int itemOwnerId = itemService.getItem(itemId).get().getOwnerId();
        if (itemOwnerId != userId) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        return itemService.editItem(itemId, item);
    }

    @GetMapping("/{itemId}")
    public Optional<Item> getItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        Optional<Item> item = itemService.getItem(itemId);
        if (item.isEmpty()) {
            throw new NoSuchItemException(Messages.NO_SUCH_ITEM);
        }

        return Optional.of(itemService.addDatesAndComments(userId, item.get()));
    }

    @GetMapping
    public List<Item> getAllMyItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        List<Item> items = itemService.getAllMyItems(userId);
        List<BookingDto> bookingDtoOwnerList = bookingService.getAllOwnerBookings(userId, State.ALL.name());
        if (items.isEmpty()) {
            return List.of();
        }
        return items.stream()
                .map(item -> ItemDatesCommentsMapper.mapFromItem(item, bookingDtoOwnerList))
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam(name = "text") String query) {
        return itemService.searchItem(query);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                  @PathVariable int itemId,
                                  @RequestBody Comment comment) {
        if (comment.getText().isEmpty()) {
            throw new EmptyCommentException(Messages.EMPTY_COMMENT);
        }
        if (!canPostComments(userId, itemId)) {
            throw new CantCommentException(Messages.ITEM_WAS_NOT_USED);
        }
        return itemService.postComment(userId, itemId, comment);
    }

    private boolean canPostComments(int userId, int itemId) {
        BookingDto bookingDto = bookingService.getAllUserBookings(userId, State.ALL.name()).stream()
                .filter(bookingDto1 -> bookingDto1.getItem().getId() == itemId)
                .min(Comparator.comparing(BookingDto::getStart)).orElse(null);

        if (bookingDto == null) {
            return false;
        }
        return !bookingDto.getStart().isAfter(Timestamp.from(Instant.now()).toLocalDateTime());
    }
}
