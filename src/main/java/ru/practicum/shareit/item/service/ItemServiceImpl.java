package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.EmptyCommentException;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemDatesCommentsMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.response.ResponseService;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Messages;
import ru.practicum.shareit.utils.Validators;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ResponseService responseService;

    @Override
    public Item saveItem(int userId, ItemDto item) {
        Validators.userPresenceValidator(userId, userRepository);
        Item itemEntity = itemRepository.itemFromDto(item);
        itemEntity.setOwnerId(userId);
        itemEntity = itemRepository.save(itemEntity);
        if (item.getRequestId() != null) {
            responseService.addResponse(item.getRequestId(), itemEntity.getId());
        }
        return itemEntity;
    }

    @Override
    public Item editItem(int userId, int itemId, ItemDto item) {
        Item myItem = Validators.returnItemIfValid(itemId, itemRepository);
        Validators.checkIfUserOwnItem(userId, myItem.getOwnerId());
        itemRepository.updateItem(item, myItem);
        return itemRepository.save(myItem);
    }

    @Override
    public Item getItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NoSuchItemException(Messages.NO_SUCH_ITEM));
    }

    @Override
    public Item addDatesAndComments(int userId, Item item) {
        int ownerId = item.getOwnerId();
        List<BookingDto> bookingDtoListOwner = bookingService.getAllOwnerBookings(ownerId, State.ALL.name());
        ItemDatesCommentsDto itemDto = ItemDatesCommentsMapper.mapFromItem(item, bookingDtoListOwner);

        List<CommentDto> comments = commentRepository.findAll().stream()
                .filter(comment -> comment.getItemId() == item.getId())
                .map(comment -> CommentDto.mapToDto(comment, userService))
                .collect(Collectors.toList());
        if (!comments.isEmpty()) {
            itemDto.setComments(comments);
        }

        if (item.getOwnerId() != userId) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }

        return itemDto;
    }

    @Override
    public List<Item> getAllMyItems(int userId) {
        List<Item> items = itemRepository.findAll().stream()
                .filter(item -> item.getOwnerId() == userId)
                .sorted(Comparator.comparingInt(Item::getId))
                .collect(Collectors.toList());
        if (items.isEmpty()) {
            return List.of();
        }
        List<BookingDto> bookingDtoOwnerList = bookingService.getAllOwnerBookings(userId, State.ALL.name());
        return items.stream()
                .map(item -> ItemDatesCommentsMapper.mapFromItem(item, bookingDtoOwnerList))
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
        if (comment.getText().isEmpty()) {
            throw new EmptyCommentException(Messages.EMPTY_COMMENT);
        }
        Validators.checkIfCanPostComments(userId, itemId, bookingService);
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        comment.setCreated(Timestamp.from(Instant.now()).toLocalDateTime());
        return CommentDto.mapToDto(commentRepository.save(comment), userService);
    }

}
