package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.Request;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(int userId, Request request) {
        return post("", userId, request);
    }

    public ResponseEntity<Object> getMyRequests(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequest(int userId, int requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllRequests(int userId, Integer from, Integer size) {
        Map<String, Object> params = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", (long) userId, params);
    }
}
