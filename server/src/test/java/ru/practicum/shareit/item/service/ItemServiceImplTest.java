package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.EmptyCommentException;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Captor
    private final ArgumentCaptor<ItemDto> itemDtoCaptor = ArgumentCaptor.forClass(ItemDto.class);
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void editItem_setDescriptionToUpdated() {
        int itemId = 0;
        int userId = 0;
        Item item = new Item();
        item.setOwnerId(userId);
        ItemDto updateItem = ItemDto.builder()
                .description("update")
                .build();
        InOrder order = inOrder(itemRepository);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        itemService.editItem(userId, itemId, updateItem);
        verify(itemRepository).updateItem(itemDtoCaptor.capture(), any(Item.class));
        assertEquals("update", itemDtoCaptor.getValue().getDescription());
        order.verify(itemRepository).updateItem(any(), any());
        order.verify(itemRepository).save(any());
    }

    @Test
    void getItem_whenItemFound_thenReturnItem() {
        int itemId = 1;
        Item expectedItem = new Item();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        Item actualItem = itemService.getItem(itemId);
        assertEquals(expectedItem, actualItem);
    }

    @Test
    void getItem_whenItemNotFound_thenThrowNoSuchItemException() {
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        assertThrows(NoSuchItemException.class, () -> itemService.getItem(itemId));
    }

    @Test
    void addDatesAndComments_checkCommentAuthorName_checkBookingsArePresent() {
        int userId = 0;
        int itemId = 0;
        Item item = new Item();
        item.setOwnerId(userId);
        item.setId(itemId);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.MIN);
        bookingDto.setEnd(LocalDateTime.MAX);
        bookingDto.setItem(new ItemBookingDto(itemId, "booking"));
        bookingDto.setBooker(new Booker(userId));
        Comment comment = new Comment();
        comment.setItemId(itemId);
        comment.setText("test");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthorId(userId);
        User user = new User();
        user.setName("name");
        when(commentRepository.findAll()).thenReturn(List.of(comment));
        when(bookingService.getAllOwnerBookings(userId, State.ALL.name())).thenReturn(List.of(bookingDto));
        when(userService.getUser(anyInt())).thenReturn(user);
        ItemDatesCommentsDto value = itemService.addDatesAndComments(userId, item);
        assertEquals("name", value.getComments().get(0).getAuthorName());
        assertNotNull(value.getLastBooking());
        assertNotNull(value.getNextBooking());
    }

    @Test
    void getAllMyItems_returnListOfOneItem() {
        int userId = 0;
        Item item = new Item();
        item.setOwnerId(userId);
        when(itemRepository.findAll()).thenReturn(List.of(item));
        assertEquals(1, itemService.getAllMyItems(userId).size());
    }

    @Test
    void getAllMyItems_returnEmptyListIfItemsNotFound() {
        int userId = 0;
        Item item = new Item();
        item.setOwnerId(userId);
        when(itemRepository.findAll()).thenReturn(List.of());
        assertEquals(List.of(), itemService.getAllMyItems(userId));
    }

    @Test
    void getAllMyItems_getWithPagination() {
        int userId = 0;
        int from = 0;
        int size = 1;
        Item itemOne = new Item();
        itemOne.setOwnerId(userId);
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(userId, PageRequest.of(from, size)))
                .thenReturn(new PageImpl<>(List.of(itemOne)));
        assertEquals(List.of(itemOne), itemService.getAllMyItems(userId, from, size));
    }

    @Test
    void testSearchItem_returnEmptyList_ifQueryIsEmpty() {
        assertEquals(List.of(), itemService.searchItem(""));
    }

    @Test
    void postComment_throwException_emptyComment() {
        int userId = 0;
        int itemId = 0;
        Comment comment = new Comment();
        assertThrows(EmptyCommentException.class, () -> itemService.postComment(userId, itemId, comment));
    }
}