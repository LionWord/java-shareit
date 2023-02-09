package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exceptions.ErrorHandler;
import ru.practicum.shareit.exceptions.NoSuchItemException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ShareItServer.class
)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql({"/drop_schema.sql", "/schema.sql", "/test_data.sql"})
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig({ItemController.class, ItemServiceImpl.class, ErrorHandler.class, ItemRepository.class})
class ItemControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;
    private Item item;
    private ItemDto itemDto;
    private int userId;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        itemDto = ItemDto.builder()
                .name("trumpet")
                .description("annoy your neighbors!")
                .available(true)
                .build();
        item = new Item();
        item.setId(2);
        item.setName("trumpet");
        item.setDescription("annoy your neighbors!");
        item.setAvailable(true);
        item.setOwnerId(2);
    }

    @Test
    void addItem_statusIsOK_correctDeserialization() throws Exception {
        userId = 1;
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(item.getId()))
                .andExpect(jsonPath("name").value(item.getName()))
                .andExpect(jsonPath("description").value(item.getDescription()))
                .andExpect(jsonPath("available").value(item.getAvailable()));
    }

    @Test
    void addItem_badRequest_userDoNotExist() throws Exception {
        userId = 99;
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void editItem_shouldChangeNameToMower() throws Exception {
        int itemId = 1;
        int userId = 1;
        String newName = "mower";
        ItemDto newItem = ItemDto.builder()
                .name(newName)
                .build();
        mvc.perform(patch("/items/" + itemId)
                        .content(mapper.writeValueAsString(newItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(newName))
                .andExpect(jsonPath("id").value(itemId));
    }

    @Test
    void editItem_notFound_editingNotByOwner_throwNoSuchItemException() throws Exception {
        int itemId = 1;
        int userId = 2;
        String newName = "mower";
        ItemDto newItem = ItemDto.builder()
                .name(newName)
                .build();
        mvc.perform(patch("/items/" + itemId)
                        .content(mapper.writeValueAsString(newItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchItemException));
    }

    @Test
    void getItem_getItemWithId1_correctDeserialization() throws Exception {
        int itemId = 1;
        userId = 1;
        mvc.perform(get("/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("drill"))
                .andExpect(jsonPath("description").value("super drill"))
                .andExpect(jsonPath("id").value(itemId))
                .andExpect(jsonPath("available").value(true));
    }

    @Test
    void getAllMyItems_shouldReturnEmptyList_userDoNotOwnAnyItem() throws Exception {
        userId = 2;
        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("[]", result.getResponse().getContentAsString()));

    }

    @Test
    void getAllMyItems_shouldReturnOneItem_nameIsDrill() throws Exception {
        userId = 1;
        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("drill")));
    }

    @Test
    void searchItem_shouldReturnDrill_queryCaseIsWeird() throws Exception {
        String requestParam = "drill";
        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", requestParam))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("drill")));
    }

    @Test
    void postComment_saveAndReturnCorrectComment() throws Exception {
        int userId = 2;
        int itemId = 1;
        Comment comment = new Comment();
        comment.setId(1);
        comment.setAuthorId(userId);
        comment.setItemId(1);
        comment.setText("Wonderful!");
        mvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("text").value("Wonderful!"))
                .andExpect(jsonPath("itemId").value(1))
                .andExpect(jsonPath("authorId").value(2));

    }
}