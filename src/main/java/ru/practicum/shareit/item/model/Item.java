package ru.practicum.shareit.item.model;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class Item {
    private String name;
    private String description;
    private int id;
    private int ownerId;
    private boolean isAvailable;

}
