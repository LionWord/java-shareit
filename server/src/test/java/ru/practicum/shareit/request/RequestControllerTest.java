package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.RequestWithResponsesDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.response.model.Response;
import ru.practicum.shareit.response.service.ResponseServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@SpringJUnitWebConfig({Request.class,
        RequestController.class,
        RequestMapper.class,
        RequestWithResponsesDto.class,
        RequestServiceImpl.class,
        ItemWithRequestDto.class,
        ItemWithRequestDto.ItemWithRequestDtoBuilder.class,
        ItemDto.class,
        ItemController.class,
        ItemServiceImpl.class,
        Response.class,
        ResponseServiceImpl.class})
class RequestControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private Request request;
    private int userId;
    private MockMvc mvc;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
        request = new Request();
        request.setDescription("i want cookies!");
    }

    @Test
    void addRequest_returnCorrectRequest() throws Exception {
        int expectedRequestId = 1;
        userId = 1;
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedRequestId))
                .andExpect(jsonPath("description").value(request.getDescription()))
                .andExpect(jsonPath("requesterId").value(userId))
                .andExpect(jsonPath("created").isNotEmpty());
    }

    @Test
    void addRequest_shouldThrowException_userDoNotExist() throws Exception {
        userId = 99;
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof NoSuchUserException));
    }

    @Test
    void getMyRequests_shouldReturnProperRequest() throws Exception {
        userId = 1;
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId));
        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("i want cookies!")));
    }

    @Test
    void getMyRequests_getByUserWithoutRequests_shouldReturnEmpty() throws Exception {
        userId = 2;
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1));
        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("[]", result.getResponse().getContentAsString()));
    }

    @Test
    void getRequest_getProperDto_withoutItems() throws Exception {
        userId = 1;
        int expectedRequestId = 1;
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId));
        mvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedRequestId))
                .andExpect(jsonPath("description").value(request.getDescription()))
                .andExpect(jsonPath("created").isNotEmpty())
                .andExpect(jsonPath("items").exists());
    }

    @Test
    void getRequest_getProperDto_withItem() throws Exception {
        ItemDto item = new ItemDto();
        item.setName("cookie");
        item.setAvailable(true);
        item.setDescription("there are your cookies!");
        item.setRequestId(1);
        userId = 1;
        int expectedRequestId = 1;
        int responderId = 2;
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId));
        mvc.perform(post("/items")
                .content(mapper.writeValueAsString(item))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", responderId));
        mvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(expectedRequestId))
                .andExpect(jsonPath("description").value(request.getDescription()))
                .andExpect(jsonPath("created").isNotEmpty())
                .andExpect(jsonPath("items").isNotEmpty());


    }

    @Test
    void getAllRequests() throws Exception {
        userId = 1;
        postMultipleRequests(userId);
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("i want cookies!"))
                .andExpect(result -> result.getResponse().getContentAsString().contains("i want toys!"))
                .andExpect(result -> result.getResponse().getContentAsString().contains("i want babies!"));

    }

    private void postMultipleRequests(int userId) throws Exception {
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId));
        request.setDescription("i want toys!");
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId));
        request.setDescription("i want babies!");
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", userId));
    }
}