package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    /**
     * TODO add database mapper when necessary.
     */
    public static User makeUserFromDB(ResultSet rs) throws SQLException {
        return User.builder().build();
    }

    public static User makeUserFromDto(int userId, UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .id(userId)
                .build();
    }

    public static UserDto makeDtoFromUser(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

}
