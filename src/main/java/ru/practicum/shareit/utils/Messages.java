package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {

    public static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final String NO_SUCH_USER = "No such user";
    public static final String NO_SUCH_ITEM = "No such item";
    public static final String NO_SUCH_BOOKING = "No such booking";
    public static final String NOT_AVAILABLE = "Item is not available";
    public static final String INVALID_ITEM_INPUT = "Invalid item input. Check if all fields are correct";
    public static final String WRONG_OWNER = "User with this ID do not own this item";
    public static final String WRONG_TIMESTAMP = "Wrong start or end booking params";
    public static final String ALREADY_APPROVED = "Booking was already approved";
    public static final String UNKNOWN_STATE = "Unknown state: ";
}
