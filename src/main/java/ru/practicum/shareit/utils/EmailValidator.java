package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailValidator {

    /**
     * Email validator. Checks if email is present, not empty and matches email pattern.
     */
    public static boolean isValidEmail(UserDto userDto) {
        String regex = ".+@.+\\..+";
        Optional<String> email = Optional.ofNullable(userDto.getEmail());
        return email.map(s -> !s.matches(regex)).orElse(true);
    }
}
