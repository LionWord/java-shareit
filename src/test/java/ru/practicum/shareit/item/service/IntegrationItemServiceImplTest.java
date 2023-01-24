package ru.practicum.shareit.item.service;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

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
    public void testSearchItem_returnItem_byQuery_Drill_caseInsensitive() {
        int userId = 1;
        ItemDto item = ItemDto.builder()
                .name("Drill")
                .description("super drill")
                .available(true)
                .build();
        itemService.saveItem(userId, item);
        assertEquals(1, itemService.searchItem("drill").size());
    }

}
