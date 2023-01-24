package ru.practicum.shareit.item.service;

import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest (
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ShareItApp.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql({"/schema.sql", "/test_data.sql"})
public class IntegrationItemServiceImplTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemServiceImpl itemService;

    @Test
    public void saveItem_returnItem() {
        /*int userId = 0;
        ItemDto item = ItemDto.builder().build();
        Item expected = new Item();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(itemRepository.itemFromDto(item)).thenReturn(new Item());
        when(itemRepository.save(any(Item.class))).thenReturn(new Item());
        assertEquals(expected, itemService.saveItem(userId, item));
        verify(itemRepository, atLeastOnce()).save(any(Item.class));*/
    }

    @Test
    public void testSearchItem_returnItem_byQuery_Drill_caseInsensitive() {
        int userId = 1;
        assertEquals(1, itemService.searchItem("drill").size());
    }

    @Test
    public void postComment() {
        Comment comment = new Comment();
    }

}
