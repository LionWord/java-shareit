package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.IdAssigner;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserDaoImplInMemory implements UserDao {

    final Map<Integer, User> users = new HashMap<>();

    public User addUser(UserDto userDto) {
        User user = UserMapper.makeUserFromDto(0, userDto);
        IdAssigner.assignUserId(user);
        users.put(user.getId(), user);
        return user;
    }

    public User editUser(int userId, UserDto userDto) {
        User user = users.get(userId);
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        users.put(userId, user);
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
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean idIsPresent(int userId) {
        return users.values().stream()
                .anyMatch(user -> user.getId() == userId);
    }


}
