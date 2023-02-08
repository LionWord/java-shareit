package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto user) {
        return userClient.createUser(user);
    }

    @GetMapping("/{userId}")
    @Cacheable(value = "users")
    public ResponseEntity<Object> getUser(@PathVariable @NotNull @Positive int userId) {
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @PatchMapping("/{userId}")
    @CachePut(value = "users")
    public ResponseEntity<Object> editUser(@PathVariable @NotNull @Positive int userId, @RequestBody UserDto user) {
        return userClient.editUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    @CacheEvict(value = "users")
    public ResponseEntity<Object> deleteUser(@PathVariable @NotNull @Positive int userId) {
        return userClient.deleteUser(userId);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Scheduled(initialDelayString = "${cache.ttl}", fixedDelayString = "${cache.ttl}")
    public void evictAllCache() {
    }

}
