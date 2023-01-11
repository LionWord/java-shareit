package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemBookingDatesDto;
import ru.practicum.shareit.item.mapper.ItemBookingDatesMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Messages;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
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
        if (itemOwnerId != userId ) {
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
        List<BookingDto> bookingDtoListOwner = bookingService.getAllOwnerBookings(userId, State.ALL);
        ItemBookingDatesDto alteredItem = ItemBookingDatesMapper.mapFromItem(item.get(), bookingDtoListOwner);
        if (item.get().getOwnerId() == userId) {
            return Optional.of(alteredItem);
        }
        alteredItem.setLastBooking(null);
        alteredItem.setNextBooking(null);
        return Optional.of(alteredItem);
    }

    @GetMapping
    public List<Item> getAllMyItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        List<Item> items = itemService.getAllMyItems(userId);
        List<BookingDto> bookingDtoOwnerList = bookingService.getAllOwnerBookings(userId, State.ALL);
        if (items.isEmpty()) {
            return List.of();
        }
        return items.stream()
                .map(item -> ItemBookingDatesMapper.mapFromItem(item, bookingDtoOwnerList))
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
            throw new IllegalArgumentException("Empty comment");
        }
        if (!canPostComments(userId, itemId)) {
            throw new IllegalArgumentException("Can't post comments");
        }
        return itemService.postComment(userId, itemId, comment);
    }
    //------Service methods---------

    private boolean canPostComments(int userId, int itemId) {
        BookingDto bookingDto = bookingService.getAllUserBookings(userId, State.ALL).stream()
                .filter(bookingDto1 -> bookingDto1.getItem().getId() == itemId)
                .sorted((Comparator.comparing(BookingDto::getStart)))
                .findFirst().orElse(null);

        if (bookingDto == null) {
            return false;
        }
        LocalDateTime start = bookingDto.getStart().toLocalDateTime();
        LocalDateTime now = Timestamp.from(Instant.now()).toLocalDateTime().plusHours(3);

        if (start.isAfter(now)) {
            return false;
        }
        return true;
    }
}
