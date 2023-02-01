package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
@DynamicUpdate
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(name = "item_name")
    @Length(max = 64)
    private String name;

    @NotEmpty
    @Column(name = "item_description")
    @Length(max = 256)
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
