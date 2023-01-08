package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "item_id")
    private int id;

    @NotNull
    @Column(name = "item_name")
    private String name;

    @NotNull
    @Column(name = "item_description")
    private String description;

    @NotNull
    @Column(name = "item_is_available")
    private Boolean available;

    @NotNull
    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "request_id")
    private int requestId;


}
