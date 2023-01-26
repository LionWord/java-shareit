package ru.practicum.shareit.item.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ShareItApp.class
)
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Sql({"/drop_schema.sql", "/schema.sql", "/test_data.sql"})
public class IntegrationItemServiceImplTest {
    @Autowired
    private ItemServiceImpl itemService;

    @Test
    public void saveItem_returnItem_nameEqualsDrum() {
        int userId = 1;
        ItemDto item = ItemDto.builder()
                .name("Drum")
                .description("Drum set")
                .available(true)
                .build();
        assertEquals("Drum", itemService.saveItem(userId, item).getName());
    }

    @Test
    public void testSearchItem_returnItem_byQuery_Drill_caseInsensitive() {
        int userId = 1;
        assertEquals(userId, itemService.searchItem("drill").size());
    }

    @Test
    public void postComment_returnCommentWithText_newComment() {
        int authorId = 2;
        int itemId = 1;
        Comment comment = new Comment();
        comment.setText("new comment");
        comment.setCreated(LocalDateTime.now());
        comment.setItemId(itemId);
        comment.setAuthorId(authorId);
        assertEquals("new comment", itemService.postComment(authorId, itemId, comment).getText());
    }

}
