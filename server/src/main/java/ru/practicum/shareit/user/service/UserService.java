package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User saveUser(UserDto user);

    User modifyUser(int userId, UserDto user);

    void deleteUser(int userId);

    User getUser(int userId);

    List<User> getAllUsers();

}
