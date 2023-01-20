package ru.practicum.shareit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {

    public static final String NO_SUCH_USER = "No such user";
    public static final String NO_SUCH_ITEM = "No such item";
    public static final String NO_SUCH_BOOKING = "No such booking";
    public static final String NOT_AVAILABLE = "Item is not available";
    public static final String WRONG_TIMESTAMP = "Wrong start or end booking params";
    public static final String ALREADY_APPROVED = "Booking was already approved";
    public static final String UNKNOWN_STATE = "Unknown state: ";
    public static final String SELF_OWNED_ITEM = "Item owner can't book own items";
    public static final String EMPTY_COMMENT = "Comment can't be empty";
    public static final String ITEM_WAS_NOT_USED = "Can't post comments about item that was not used by user";
    public static final String EMPTY_REQUEST = "Request can't be empty";
}
