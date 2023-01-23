package ru.practicum.shareit.item.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.response.ResponseService;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Validators;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ResponseService responseService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void saveItem() {

    }

    @Test
    void editItem() {
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
    void addDatesAndComments() {
    }

    @Test
    void getAllMyItems() {
    }

    @Test
    void testGetAllMyItems() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void testSearchItem() {
    }

    @Test
    void postComment() {
    }
}