package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDatesCommentsMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Messages;

import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingService bookingService;

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
    public Item addDatesAndComments(int userId, Item item) {
        int ownerId = item.getOwnerId();
        List<BookingDto> bookingDtoListOwner = bookingService.getAllOwnerBookings(ownerId, State.ALL);
        ItemDatesCommentsDto itemDto = ItemDatesCommentsMapper.mapFromItem(item, bookingDtoListOwner);

        List<CommentDto> comments = commentRepository.findAll().stream()
                .filter(comment -> comment.getItemId() == item.getId())
                .map(comment -> CommentDto.MapToDto(comment, userService))
                .collect(Collectors.toList());
        if (!comments.isEmpty()) {
            itemDto.setComments(comments);
        }

        if (item.getOwnerId() != userId) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }

        return itemDto;
    };

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

    @Override
    public CommentDto postComment(int userId, int itemId, Comment comment) {
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        comment.setCreated(Timestamp.from(Instant.now()).toLocalDateTime());
        CommentDto commentDto = CommentDto.MapToDto(commentRepository.save(comment), userService);
        return commentDto;
    }

}
