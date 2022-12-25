package ru.practicum.shareit.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class User {
    private String name;
    private String email;
    private int id;
}
