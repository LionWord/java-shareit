package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.IdAssigner;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface UserDao {

    User addUser (UserDto userDto);

    User editUser(int userId, UserDto userDto);

    void deleteUser(int userId);

    User getUser(int userId);

    List<User> getAllUsers();

    boolean emailIsPresent(String email);

    boolean idIsPresent(int userId);
}
