package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder(access = AccessLevel.PUBLIC)
public class Item {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private int id;
    @NotNull
    private int ownerId;
    @NotNull
    private Boolean available;

}
