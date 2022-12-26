package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.EmailValidator;
import ru.practicum.shareit.utils.Messages;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        if (EmailValidator.isValidEmail(userDto)) {
            throw new InvalidEmailException(Messages.INVALID_EMAIL);
        } else if (userService.emailIsPresent(userDto.getEmail())) {
            throw new EmailAlreadyExistsException(Messages.EMAIL_ALREADY_EXISTS);
        }
        return userService.addUser(userDto);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        if (userService.idIsPresent(userId)) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        return userService.getUser(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public User editUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        if (userService.idIsPresent(userId)) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        User oldUser = userService.getUser(userId);
        if (!oldUser.getEmail().equals(userDto.getEmail()) & userDto.getEmail() != null) {
            if (EmailValidator.isValidEmail(userDto)) {
                throw new InvalidEmailException(Messages.INVALID_EMAIL);
            }
            if (userService.emailIsPresent(userDto.getEmail())) {
                throw new EmailAlreadyExistsException(Messages.EMAIL_ALREADY_EXISTS);
            }
        }

        return userService.editUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        if (userService.idIsPresent(userId)) {
            throw new NoSuchUserException(Messages.NO_SUCH_USER);
        }
        userService.deleteUser(userId);
    }

}
