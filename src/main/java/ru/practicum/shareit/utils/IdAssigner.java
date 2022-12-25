package ru.practicum.shareit.utils;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class IdAssigner {

    static private int userId = 1;
    static private int itemId = 1;

    public static void assignUserId(User user) {
        user.setId(userId);
        userId++;
    }
    public static void assignItemId(Item item) {
        item.setId(itemId);
        userId++;
    }
    public static int getUserIdValue() {
        return userId;
    }
    public static int getItemId() {
        return itemId;
    }
}
