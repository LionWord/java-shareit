package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.InvalidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.EmailValidator;
import ru.practicum.shareit.utils.Messages;

import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(UserDto userDto) {
        if (userService.emailIsPresent(userDto.getEmail())) {
            throw new EmailAlreadyExistsException(Messages.EMAIL_ALREADY_EXISTS);
        } else if (!EmailValidator.isValidEmail(userDto)) {
            throw new InvalidEmailException(Messages.INVALID_EMAIL);
        }
        return userService.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    public User editUser(@PathVariable int userId) {
        return userService.editUser(userId);
    }



}
