package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                  @RequestBody @Valid ItemDto item) {
        return itemClient.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                         @PathVariable @Positive int itemId,
                         @RequestBody ItemDto item) {
        return itemClient.editItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                          @PathVariable @Positive int itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllMyItems(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                    @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                    @RequestParam(name = "size", required = false) @Positive Integer size) {
        return itemClient.getAllMyItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                             @RequestParam(name = "text") String query,
                                             @RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                             @RequestParam(name = "size", required = false) @Positive Integer size) {
        return itemClient.searchItem(userId, query, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") @NotNull @Positive int userId,
                                  @PathVariable @Positive int itemId,
                                  @RequestBody @Valid Comment comment) {
        return itemClient.postComment(userId, itemId, comment);
    }

}
