package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {

    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String INVALID_EMAIL = "Invalid email";
}
