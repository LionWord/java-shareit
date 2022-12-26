package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {

    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String NO_SUCH_USER = "No such user";
    public static final String INVALID_ITEM_INPUT = "Invalid item input. Check if all fields are correct";
    public static final String WRONG_USER = "User with this ID do not own this item";
}
