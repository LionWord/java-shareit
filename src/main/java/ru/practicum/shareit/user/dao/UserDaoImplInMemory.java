package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.IdAssigner;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDaoImplInMemory implements UserDao {

    Map<Integer, User> users = Map.of();

    public User addUser (UserDto userDto) {
        User user = UserMapper.makeUserFromDto(0, userDto);
        IdAssigner.assignUserId(user);
        users.put(user.getId(), user);
        return user;
    }

    public User editUser(int userId) {
        users.put(userId, users.get(userId));
        return getUser(userId);
    }

    public void deleteUser(int userId) {
        users.remove(userId);
    }

    public User getUser(int userId) {
        return users.get(userId);
    }

    public List<User> getAllUsers() {
        return users.values().stream()
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public boolean emailIsPresent(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail() == email);
    }


}
