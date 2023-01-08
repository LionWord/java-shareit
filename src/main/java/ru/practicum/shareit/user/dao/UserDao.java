package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {

    User addUser(UserDto userDto);

    User editUser(int userId, UserDto userDto);

    void deleteUser(int userId);

    User getUser(int userId);

    List<User> getAllUsers();

    boolean emailIsPresent(String email);

    boolean idIsPresent(int userId);
}
