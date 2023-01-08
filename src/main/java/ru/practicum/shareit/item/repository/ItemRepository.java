package ru.practicum.shareit.item.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.mapper.ItemMapperDpa;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>, ItemMapperDpa {
    List<Item> findAllByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(@NotNull String description, @NotNull String name);
}
