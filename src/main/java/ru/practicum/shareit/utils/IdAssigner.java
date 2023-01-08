package ru.practicum.shareit.utils;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class IdAssigner {

    private static int userId = 1;
    private static int itemId = 1;

    public static void assignUserId(User user) {
        user.setId(userId);
        userId++;
    }

    public static void assignItemId(Item item) {
        item.setId(itemId);
        itemId++;
    }
}
