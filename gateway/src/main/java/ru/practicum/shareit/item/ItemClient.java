package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(int userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> editItem(int userId, int itemId, ItemDto item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> getItem(int userId, int itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllMyItems(int userId, Integer from, Integer size) {
        Map<String, Object> params;
        if (from == null | size == null) {
            return get("", userId);
        }
        params = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", (long) userId, params);
    }

    public ResponseEntity<Object> searchItem(int userId, String query, Integer from, Integer size) {
        Map<String, Object> params;
        if (from == null | size == null) {
            return get("/search?text={query}", (long) userId, Map.of("query", query));
        }
        params = Map.of(
                "query", query,
                "from", from,
                "size", size
        );
        return get("/search?text={query}&from={from}&size={size}", (long) userId, params);
    }

    public ResponseEntity<Object> postComment(int userId, int itemId, Comment comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }


}
