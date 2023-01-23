package ru.practicum.shareit.item.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.response.ResponseService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

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
    @Captor
    private ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
    @Captor
    private ArgumentCaptor<ItemDto> itemDtoCaptor = ArgumentCaptor.forClass(ItemDto.class);
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void saveItem_returnItem() {
        int userId = 0;
        ItemDto item = ItemDto.builder().build();
        Item expected = new Item();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.itemFromDto(item)).thenReturn(new Item());
        when(itemRepository.save(any(Item.class))).thenReturn(new Item());
        assertEquals(expected, itemService.saveItem(userId, item));
        verify(itemRepository, atLeastOnce()).save(any(Item.class));
    }

    @Test
    void editItem_setDescriptionToUpdated() {
        /*int userId = 0;
        int itemId = 0;

        Item validationItem = new Item();
        validationItem.setOwnerId(userId);
        validationItem.setId(itemId);
        User validationUser = new User();
        validationUser.setId(userId);
        ItemDto item = ItemDto.builder()
                .description("updated")
                .build();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(validationItem));
        assertEquals("updated", itemService.editItem(userId, itemId, item).getDescription());
        verify(itemRepository).updateItem(itemDtoCaptor.capture(), itemCaptor.capture());
        assertEquals(item, itemDtoCaptor.getValue());
        assertEquals(new Item(), itemCaptor.getValue());*/
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